import axios from "axios";
import {getLocalStorageItem, removeLocalStorageItem, setLocalStorageItem} from "./hooks/HookLocalStorage.js";

const instance = axios.create({
    baseURL: "http://51.77.59.237:8080",
    headers: {
        "Content-Type": "application/json",
    }
})

const getRefreshToken = () => {
    return getLocalStorageItem("refresh_token");
}

const getAccessToken = () => {
    return getLocalStorageItem("access_token");
}

const setAccessToken = (accessToken) => {
    setLocalStorageItem("access_token", accessToken);
}

const refreshAccessToken = async () => {
    const response = await instance.post("/auth/access-token", {
        refresh_token: getRefreshToken()
    });
    setAccessToken(response.data.access_token);
};

instance.interceptors.request.use((config) => {
    // Modify the request config
    config.headers.Authorization = `Bearer ${getAccessToken()}`;
    return config;
});

// Axios interceptor to handle expired tokens
instance.interceptors.response.use(
    (response) => response,
    (error) => {
        const originalRequest = error.config;

        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;
            return refreshAccessToken().then(() => {
                originalRequest.headers.Authorization = `Bearer ${getAccessToken()}`;
                return instance(originalRequest);
            }).catch(() => {
                removeLocalStorageItem("access_token");
                removeLocalStorageItem("refresh_token");
                return Promise.reject(error);
            });
        }
        return Promise.reject(error);
    }
);

export default instance;
