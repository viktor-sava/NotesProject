import {configureStore} from "@reduxjs/toolkit";
import {authReducer} from "./slices/AuthReducer.js";
import {notesReducer} from "./slices/NoteReducer.js";
import {useDispatch, useSelector} from "react-redux";

const store = configureStore({
    reducer: {
        auth: authReducer,
        notes: notesReducer
    }
});

export const useAppDispatch = useDispatch
export const useAppSelector = useSelector

export default store;