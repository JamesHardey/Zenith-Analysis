//package com.jcoding.zenithanalysis.controller.impl;
//
//import com.jcoding.zenithanalysis.entity.AppUser;
//import com.jcoding.zenithanalysis.services.AppUserServices;
//import org.aspectj.lang.annotation.Before;
//import org.hamcrest.Matchers;
//import org.hamcrest.core.Is;
//import org.junit.jupiter.api.Test;
//import org.mockito.ArgumentMatchers;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
//public class AppUserControllerImplTest {
//    //TODO: create the data Test generator class AppUserBuilder
//    private static final String ENDPOINT_URL = "/app-users";
//    @MockBean
//    private ReferenceMapper referenceMapper;
//    @InjectMocks
//    private AppUserControllerImpl appuserController;
//
//    @MockBean
//    private AppUserServices appuserService;
//    @MockBean
//    private AppUserMapper appuserMapper;
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Before
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.standaloneSetup(this.appuserController).build();
//    }
//
//    @Test
//    public void getAll() throws Exception {
//        Mockito.when(appuserMapper.asDTOList(ArgumentMatchers.any())).thenReturn(AppUserBuilder.getListDTO());
//
//        Mockito.when(appuserService.getAllCourses()).thenReturn(AppUserBuilder.getListEntities());
//        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content()
//                        .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));
//
//    }
//
//    @Test
//    public void getById() throws Exception {
//        Mockito.when(appuserMapper.asDTO(ArgumentMatchers.any())).thenReturn(AppUserBuilder.getDTO());
//
//        Mockito.when(appuserService.findById(ArgumentMatchers.anyLong())).thenReturn(java.util.Optional.of(AppUserBuilder.getEntity()));
//
//        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT_URL + "/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content()
//                        .contentType(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Is.is(1)));
//        Mockito.verify(appuserService, Mockito.times(1)).findById(1L);
//        Mockito.verifyNoMoreInteractions(appuserService);
//    }
//
//    @Test
//    public void save() throws Exception {
//        Mockito.when(appuserMapper.asEntity(ArgumentMatchers.any())).thenReturn(AppUserBuilder.getEntity());
//        Mockito.when(appuserService.save(ArgumentMatchers.any(AppUser.class))).thenReturn(AppUserBuilder.getEntity());
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post(ENDPOINT_URL)
//                                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                                .content(CustomUtils.asJsonString(AppUserBuilder.getDTO())))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//        Mockito.verify(appuserService, Mockito.times(1)).save(ArgumentMatchers.any(AppUser.class));
//        Mockito.verifyNoMoreInteractions(appuserService);
//    }
//
//    @Test
//    public void update() throws Exception {
//        Mockito.when(appuserMapper.asEntity(ArgumentMatchers.any())).thenReturn(AppUserBuilder.getEntity());
//        Mockito.when(appuserService.update(ArgumentMatchers.any(), ArgumentMatchers.anyLong())).thenReturn(AppUserBuilder.getEntity());
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.put(ENDPOINT_URL + "/1")
//                                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                                .content(CustomUtils.asJsonString(AppUserBuilder.getDTO())))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        Mockito.verify(appuserService, Mockito.times(1)).update(ArgumentMatchers.any(AppUser.class), ArgumentMatchers.anyLong());
//        Mockito.verifyNoMoreInteractions(appuserService);
//    }
//
//    @Test
//    public void delete() throws Exception {
//        Mockito.doNothing().when(appuserService).deleteById(ArgumentMatchers.anyLong());
//        mockMvc.perform(
//                        MockMvcRequestBuilders.delete(ENDPOINT_URL + "/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//        Mockito.verify(appuserService, Mockito.times(1)).deleteById(Mockito.anyLong());
//        Mockito.verifyNoMoreInteractions(appuserService);
//    }