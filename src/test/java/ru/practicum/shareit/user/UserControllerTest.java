package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.practicum.shareit.exceptions.NotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.request.ItemRequestControllerTest.asJsonString;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(0));
    }


    @Test
    void getUserById_invalidId() throws Exception {
        when(userService.getUserById(anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.get("/users/100"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addUser() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("test")
                .email("test@test.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void addUser_DuplicateEmail() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("test")
                .email("test@test.com")
                .build();

        userService.addUser(userDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateUser_invalidId() throws Exception {
        UserDto userDto = UserDto.builder()
                .name("test")
                .email("test@test.com")
                .build();
        when(userService.updateUser(anyLong(), any(UserDto.class))).thenThrow(NotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/100")
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
