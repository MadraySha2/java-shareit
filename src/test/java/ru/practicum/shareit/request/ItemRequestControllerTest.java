package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.exceptions.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ItemRequestsService requestService;

    @InjectMocks
    private ItemRequestController requestController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(requestController).build();
    }

    @Test
    public void testAddRequest() throws Exception {
        Long userId = 1L;
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setDescription("Request Description");

        ItemRequestDto savedRequest = new ItemRequestDto();
        savedRequest.setId(1L);
        savedRequest.setDescription("Request Description");

        when(requestService.addRequest(eq(userId), any(ItemRequestDto.class))).thenReturn(savedRequest);

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRequest.getId()))
                .andExpect(jsonPath("$.description").value(savedRequest.getDescription()));
    }

    @Test
    public void testAddRequest_InvalidUser() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("Test description");

        when(requestService.addRequest(anyLong(), any(ItemRequestDto.class))).thenThrow(new NotFoundException("User not found!"));

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 0L)
                        .content(asJsonString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddRequest_InvalidDescription() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto();
        requestDto.setDescription("");

        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(asJsonString(requestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOwnRequests() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        ItemRequestWithItems request1 = new ItemRequestWithItems();
        request1.setId(1L);
        request1.setDescription("Request Description");

        ItemRequestWithItems request2 = new ItemRequestWithItems();
        request2.setId(2L);
        request2.setDescription("Request Description2");

        List<ItemRequestWithItems> ownRequests = Arrays.asList(request1, request2);

        when(requestService.getOwnRequests(eq(userId), any())).thenReturn(ownRequests);

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(request1.getId()))
                .andExpect(jsonPath("$[0].description").value(request1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(request2.getId()))
                .andExpect(jsonPath("$[1].description").value(request2.getDescription()));
    }

    @Test
    public void testGetOwnRequests_InvalidUser() throws Exception {
        int from = 0;
        int size = 10;

        when(requestService.getOwnRequests(anyLong(), any(PageRequest.class)))
                .thenThrow(new NotFoundException("User not found!"));


        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 0L)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testGetAllRequests() throws Exception {
        Long userId = 1L;
        int from = 0;
        int size = 10;

        ItemRequestWithItems request1 = new ItemRequestWithItems();
        request1.setId(1L);
        request1.setDescription("Request Description");

        ItemRequestWithItems request2 = new ItemRequestWithItems();
        request2.setId(2L);
        request2.setDescription("Request Description");

        List<ItemRequestWithItems> allRequests = Arrays.asList(request1, request2);

        when(requestService.getAll(eq(userId), any())).thenReturn(allRequests);

        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", userId)
                        .param("from", String.valueOf(from))
                        .param("size", String.valueOf(size)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(request1.getId()))
                .andExpect(jsonPath("$[0].description").value(request1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(request2.getId()))
                .andExpect(jsonPath("$[1].description").value(request2.getDescription()));
    }

    @Test
    public void testGetRequestById() throws Exception {
        Long userId = 1L;
        Long requestId = 1L;

        ItemRequestWithItems request = new ItemRequestWithItems();
        request.setId(requestId);
        request.setDescription("Request 1");

        when(requestService.getRequestById(eq(userId), eq(requestId))).thenReturn(request);

        mockMvc.perform(get("/requests/{requestId}", requestId)
                        .header("X-Sharer-User-Id", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.description").value(request.getDescription()));
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
