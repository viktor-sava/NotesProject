import {useEffect, useState} from "react";

export const setLocalStorageItem = (key, item) => {
    window.localStorage.setItem(key, item);
    window.dispatchEvent(new Event("storage"));
}

export const removeLocalStorageItem = (key) => {
    window.localStorage.removeItem(key);
    window.dispatchEvent(new Event("storage"));
}

export const getLocalStorageItem = (key) => {
    return window.localStorage.getItem(key);
}

export const useLocalStorage = () => {
    const [value, setValue] = useState(localStorage || {});

    const handle = () => {
        setValue({...localStorage});
    };

    useEffect(() => {
        window.addEventListener("storage", () => handle());

        return () => window.removeEventListener("storage", () => handle());
    }, []);

    return [value, setValue];
};