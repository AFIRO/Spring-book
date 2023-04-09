package be.hogent.springbook.user.controller;

import be.hogent.springbook.security.entity.SecurityUser;
import be.hogent.springbook.user.entity.ApplicationUser;
import be.hogent.springbook.user.entity.UserRole;
import be.hogent.springbook.user.entity.dto.ApplicationUserDto;
import be.hogent.springbook.user.entity.dto.RegisterDto;
import be.hogent.springbook.user.mapper.ApplicationUserMapper;
import be.hogent.springbook.user.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static be.hogent.springbook.TestData.getApplicationUser;
import static be.hogent.springbook.user.UserTestData.getApplicationUserDto;
import static be.hogent.springbook.user.UserTestData.getValidRegisterDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @MockBean
    private UserService userService;
    @MockBean
    private ApplicationUserMapper applicationUserMapper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAll_happyFlow() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationUserDto listContents = getApplicationUserDto();
        List<ApplicationUserDto> expected = Arrays.asList(listContents);
        when(userService.getAll()).thenReturn(expected);
        when(applicationUserMapper.toDto(any())).thenReturn(expected.get(0));
        MvcResult result = mockMvc.perform(get("/api/users").with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andReturn();
        List<ApplicationUserDto> actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual.get(0)).isEqualTo(expected.get(0));
        verify(userService, times(1)).getAll();
    }

    @Test
    void getById_happyFlow() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ApplicationUserDto expected = getApplicationUserDto();
        ApplicationUser returnData = getApplicationUser();
        when(userService.getById(expected.getUserId())).thenReturn(returnData);
        when(applicationUserMapper.toDto(returnData)).thenReturn(expected);
        MvcResult result = mockMvc.perform(get("/api/users/" + expected.getUserId()).with(user(getAdminUser())))
                .andExpect(status().isOk())
                .andReturn();
        ApplicationUserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual).isEqualTo(expected);
        verify(userService, times(1)).getById(expected.getUserId());
    }

    @Test
    void createUser_happyFlow() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterDto startData = getValidRegisterDto();
        ApplicationUserDto expected = getApplicationUserDto();
        when(userService.createUser(startData)).thenReturn(expected);

        MvcResult result = mockMvc.perform(post("/api/users")
                        .with(user(getAdminUser()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(startData))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andReturn();
        ApplicationUserDto actual = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
        });
        assertThat(actual).isEqualTo(expected);
        verify(userService, times(1)).createUser(startData);
    }

    @Test
    void deleteUser_happyFlow() throws Exception {
        ApplicationUserDto expected = getApplicationUserDto();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/" + expected.getUserId())
                        .with(user(getAdminUser()))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(userService, times(1)).deleteUser(expected.getUserId());
    }


    private SecurityUser getAdminUser() {
        ApplicationUser appplicationUser = getApplicationUser();
        appplicationUser.setEmail("admin");
        appplicationUser.setPassword("admin");
        appplicationUser.setRole(UserRole.ADMIN);
        return new SecurityUser(appplicationUser);
    }

}