//package ru.whattime.whattime.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import ru.whattime.whattime.model.User;
//import ru.whattime.whattime.repository.UserRepository;
//
//
//@SpringBootTest
//@AutoConfigureMockMvc
//class AuthControllerTest {
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @MockBean
//    UserRepository userRepository;
//
//    @Autowired
//    ObjectMapper mapper;
//
//    @Test
//    @SneakyThrows
//    void loginTest() {
//        User user1 = new User(1L, "John", null);
//
//        Mockito.when(userRepository.save(user1)).thenReturn(user1);
//
//        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/api/v1/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(user1));
//
//        mockMvc.perform(mockRequest)
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//
//        User user2 = new User(1L, "", null);
//
//        MockHttpServletRequestBuilder mockRequest2 = MockMvcRequestBuilders.post("/api/v1/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(user2));
//
//        mockMvc.perform(mockRequest2)
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//
//    @Test
//    @SneakyThrows
//    void loginWithBadRequestTest() {
//        User user2 = new User(1L, "", null);
//
//        Mockito.when(userRepository.save(user2)).thenReturn(user2);
//
//        MockHttpServletRequestBuilder mockRequest2 = MockMvcRequestBuilders.post("/api/v1/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON)
//                .content(this.mapper.writeValueAsString(user2));
//
//        mockMvc.perform(mockRequest2)
//                .andExpect(MockMvcResultMatchers.status().isBadRequest());
//    }
//}