package com.bci.usuarios.user;

import com.bci.usuarios.exceptions.ApiRequestException;
import com.bci.usuarios.phone.PhoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getUsers() {

        List<User> users = userRepository.findAll();
        List<UserDTO> usersDTO= new ArrayList<UserDTO>();

        users.stream().forEach(user -> {
            usersDTO.add(new UserDTO(
                    user.getId(),
                    user.getCreated(),
                    user.getModified(),
                    user.getLastLogin(),
                    user.getToken(),
                    user.getIsactive()));
        });

        return usersDTO;
    }

    public UserDTO addUser(User user){

        if (user.getName() == null
                || user.getName().length() == 0){
            throw new ApiRequestException("Nombre no suministrado");
        }

        if (user.getEmail() == null
                || user.getEmail().length() == 0){
            throw new ApiRequestException("Email no suministrado");
        }

        if (user.getPassword() == null
                || user.getPassword().length() == 0){
            throw new ApiRequestException("Contraseña no suministrada");
        }

        boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if (emailExists){
            throw new ApiRequestException("El correo ya está registrado.");
        }

        if (!validaEmail(user.getEmail())){
            throw new ApiRequestException("Formato de email incorrecto.");
        }

        if (!validaPassword(user.getPassword())){
            throw new ApiRequestException(
                    "La contraseña debe contener 1 letra mayúscula, 1 letra minúscula y 2 números.");
        }

        user.setId(UUID.randomUUID().toString());

        if (user.getPhones().size()>0){
            user.getPhones().stream().forEach(phone -> {
                if (phone.getNumber() == null || phone.getNumber().length() == 0){
                    throw new ApiRequestException("número de teléfono no suministrado");
                }
                if (phone.getCitycode() == null || phone.getCitycode().length() == 0){
                    throw new ApiRequestException("Código de ciudad no suministrado");
                }
                if (phone.getCountrycode() == null || phone.getCountrycode().length() == 0){
                    throw new ApiRequestException("Código de pais no suministrado");
                }
                phone.setUser(user);
            });
        }

        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setToken(UUID.randomUUID().toString());
        user.setIsactive(false);
        userRepository.save(user);

        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getCreated(),
                null,
                user.getLastLogin(),
                user.getToken(),
                user.getIsactive());

        return userDTO;
    }

    @Transactional
    public UserDTO loginUser(String email, String password) {
        User user = userRepository.findByEmailAndPassword(email, password)
                .orElseThrow(() -> new ApiRequestException(
                        "No se puede iniciar sesión con email " + email + " y password "+ password));

        if (!user.getIsactive()){
            throw new ApiRequestException("Usuario inactivo");
        }

        user.setLastLogin(LocalDateTime.now());
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsactive());

        return userDTO;
    }

    @Transactional
    public UserDTO updateUser(
            String userId,
            String name,
            String email,
            String password) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiRequestException(
                        "Usuario con el id " + userId + " no existe"));

        if (name != null
                && name.length() >0
                && !Objects.equals(user.getName(),name)){

            user.setName(name);
        }

        if (email != null
                && email.length() >0
                && !Objects.equals(user.getEmail(),email)){

            User userEmail = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApiRequestException(
                            "El email "+ email +" está regitrado"));

            user.setEmail(email);
        }

        if (password != null
                && password.length() >0
                && !Objects.equals(user.getPassword(),password)){

            user.setPassword(password);
        }

        user.setModified(LocalDateTime.now());
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsactive());

        return userDTO;
    }

    @Transactional
    public UserDTO activateUser(
            String userId,
            String token) {

        User user = userRepository.findByIdAndToken(userId, token)
                .orElseThrow(() -> new ApiRequestException(
                        "Usuario con el id " + userId + " y token " + token + " no existe"));

        if (!user.getIsactive()){
            user.setIsactive(true);
        }

        user.setModified(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getCreated(),
                user.getModified(),
                user.getLastLogin(),
                user.getToken(),
                user.getIsactive());

        return userDTO;
    }


    public Boolean validaEmail (String email) {
        Pattern pattern = Pattern.compile("^([0-9a-zA-Z]+[-._+&])*[0-9a-zA-Z]+@([-0-9a-zA-Z]+[.])+[a-zA-Z]{2,6}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public Boolean validaPassword(String password){
        boolean resultado = false;

        int numberCount = 0;
        int capitalCount = 0;
        int lowerCount = 0;

        for (int x = 0; x < password.length(); x++) {
            if ((password.charAt(x) > 47 && password.charAt(x) < 58)) {
                numberCount++;
            }

            if ((password.charAt(x) > 64 && password.charAt(x) < 91)) {
                capitalCount++;
            }
            if ((password.charAt(x) > 97 && password.charAt(x) < 122)) {
                lowerCount++;
            }
        }

        if (capitalCount > 0 && lowerCount > 0 && numberCount > 1)  {
            resultado=true;
        }

        return resultado;
    }
}
