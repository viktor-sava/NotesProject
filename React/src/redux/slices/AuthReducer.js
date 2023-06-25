import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "../../Axios.js";
import {removeLocalStorageItem, setLocalStorageItem} from "../../hooks/HookLocalStorage.js";

export const fetchKeyPairData = createAsyncThunk("auth/fetchKeyPairData", async (params, {rejectWithValue}) => {
    try {
        const response = await axios.post("/auth/login", params);
        return response.data;
    } catch (e) {
        return rejectWithValue(e.response.data);
    }
})

export const fetchRegisterData = createAsyncThunk("auth/fetchRegisterData", async (params, {rejectWithValue}) => {
    try {
        const response = await axios.post("/auth/register", params);
        return response.data;
    } catch (e) {
        return rejectWithValue(e.response.data);
    }
})

export const fetchLogout = createAsyncThunk("auth/fetchLogout", async () => {
    const response = await axios.get("/auth/logout");
    return response.data;
})

export const fetchUserData = createAsyncThunk("auth/fetchUserData", async () => {
    const response = await axios.get("/auth/me");
    return response.data;
})

const initialState = {
    data: [],
    status: "loading"
}

const authSlice = createSlice({
    name: "auth",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchLogout.fulfilled, (state) => {
            removeLocalStorageItem("access_token");
            removeLocalStorageItem("refresh_token");
            state.status = "loading";
            state.data = [];
        });
        builder.addCase(fetchRegisterData.fulfilled, (state, action) => {
            setLocalStorageItem("access_token", action.payload.access_token);
            setLocalStorageItem("refresh_token", action.payload.refresh_token);
        });
        builder.addCase(fetchKeyPairData.fulfilled, (state, action) => {
            setLocalStorageItem("access_token", action.payload.access_token);
            setLocalStorageItem("refresh_token", action.payload.refresh_token);
        });
        builder.addCase(fetchUserData.pending, (state) => {
            state.status = 'loading';
            state.data = [];
        });
        builder.addCase(fetchUserData.fulfilled, (state, action) => {
            state.status = 'loaded';
            state.data = action.payload;
        });
        builder.addCase(fetchUserData.rejected, (state) => {
            state.status = 'error';
            state.data = [];
        });
    }
});

export const authReducer = authSlice.reducer;
