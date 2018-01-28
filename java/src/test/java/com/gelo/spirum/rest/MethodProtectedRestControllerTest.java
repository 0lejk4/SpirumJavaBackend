package com.gelo.spirum.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gelo.spirum.security.JwtTokenUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MethodProtectedRestControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void shouldGetUnauthorizedWithoutRole() throws Exception{
        this.mvc
                .perform(get("/protected"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void shouldGetForbiddenWithUserRole() throws Exception{
        this.mvc
                .perform(get("/protected"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER",username = "0lejk4")
    public void shouldAcceptFriend() throws Exception{
        PersonController.Answer answer = new PersonController.Answer();
        answer.setAnswer(true);
        this.mvc.perform(post("/answerFriendInvite/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(answer)))
                .andExpect(status().is2xxSuccessful());
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    public void shouldGetOkWithAdminRole() throws Exception{
        this.mvc
                .perform(get("/protected"))
                .andExpect(status().is2xxSuccessful());
    }

}
