import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import axios from "../../Axios.js";
import {act} from "react-dom/test-utils";

export const fetchAddTagData = createAsyncThunk("notes/fetchAddTagData", async (values) => {
    console.log(values);
    const response = await axios.post(`/notes/${values.note_id}/tags`, {
        value: values.value
    });
    return response.data;
});

export const fetchRemoveTagData = createAsyncThunk("notes/fetchRemoveTagData", async (values) => {
    const response = await axios.delete(`/notes/${values.note_id}/tags/${values.id}`);
    return response.data;
});

export const fetchNotesData = createAsyncThunk("notes/fetchNotesData", async () => {
    const response = await axios.get("/notes");
    return response.data;
});

export const fetchRemoveNoteData = createAsyncThunk("notes/fetchRemoveNoteData", async (values) => {
    const response = await axios.delete(`notes/${values.id}`);
    return response.data;
});

export const fetchAddNoteData = createAsyncThunk("notes/fetchAddNoteData", async () => {
    const response = await axios.post("/notes");
    return response.data;
});

export const fetchChangeNoteData = createAsyncThunk("notes/fetchChangeNoteData", async (values) => {
    const response = await axios.put(`notes`, {
        id: values.id,
        content: values.content,
        status: values.status
    });
    return response.data;
});

export const fetchChangeFolderNoteData = createAsyncThunk("notes/fetchChangeFolderNoteData", async (values) => {
    const response = await axios.put(`notes/${values.note_id}/folder`, {
        id: values.id
    });
    return response.data;
});

export const fetchDeleteFolderNoteData = createAsyncThunk("notes/fetchDeleteFolderNoteData", async (values) => {
    const response = await axios.delete(`notes/${values.note_id}/folder`);
    return response.data;
});

export const fetchCreateFolderNoteData = createAsyncThunk("notes/fetchCreateFolderNoteData", async (values) => {
    const response = await axios.post(`notes/${values.note_id}/folder`, {
        name: values.name
    });
    return response.data;
});

const sortNotes = (payload) => {
    const statusList = ["significant", "important", "default"];
    return payload.sort((a, b) => {
        if (a.status === b.status) return 0;
        return statusList.indexOf(a.status) - statusList.indexOf(b.status);
    });
}

const initialState = {
    data: [],
    status: "loading",
}

const noteSlice = createSlice({
    name: "notes",
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(fetchChangeFolderNoteData.fulfilled, (state, action) => {
            const {id, folder} = action.payload;
            state.data.find((note) => note.id === id).folder = folder;
        });
        builder.addCase(fetchCreateFolderNoteData.fulfilled, (state, action) => {
            const {id, folder} = action.payload;
            state.data.find((note) => note.id === id).folder = folder;
        });
        builder.addCase(fetchDeleteFolderNoteData.fulfilled, (state, action) => {
            const {id} = action.payload;
            state.data.find((note) => note.id === id).folder = null;
        });
        builder.addCase(fetchChangeNoteData.fulfilled, (state, action) => {
            state.data.find((note) => note.id === action.meta.arg.id).content = action.meta.arg.content;
            state.data.find((note) => note.id === action.meta.arg.id).status = action.meta.arg.status;
            state.data = sortNotes(state.data);
        });
        builder.addCase(fetchRemoveNoteData.fulfilled, (state, action) => {
            state.data = state.data.filter((note) => note.id !== action.meta.arg.id);
        });
        builder.addCase(fetchRemoveTagData.fulfilled, (state, action) => {
            const tags = state.data.find((note) => note.id === action.meta.arg.note_id)?.tags;
            state.data.find((note) => note.id === action.meta.arg.note_id).tags = tags?.filter((tag) => tag.id !== action.meta.arg.id);
        });
        builder.addCase(fetchAddTagData.fulfilled, (state, action) => {
            state.data.find((note) => note.id === action.meta.arg.note_id).tags.push(action.payload);
        });
        builder.addCase(fetchAddNoteData.fulfilled, (state, action) => {
            state.data.push(action.payload);
            state.data = sortNotes(state.data);
        });
        builder.addCase(fetchNotesData.pending, (state) => {
            state.status = 'loading';
            state.data = [];
        });
        builder.addCase(fetchNotesData.fulfilled, (state, action) => {
            state.status = 'loaded';
            state.data = sortNotes(action.payload);
        });
        builder.addCase(fetchNotesData.rejected, (state) => {
            state.status = 'error';
            state.data = [];
        });
    }
});

export const notesReducer = noteSlice.reducer;