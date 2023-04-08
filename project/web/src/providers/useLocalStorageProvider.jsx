import { useEffect, useState } from "react";

const useLocalStorageProvider = (key, initialValue) => {
  const [value, setValue] = useState(() => {
    try {
      const data = window.localStorage.getItem(key);
      return data ? JSON.parse(data) : initialValue;
    } catch {
      return initialValue;
    }
  });

  useEffect(() => {
    window.localStorage.setItem(key, JSON.stringify(value));
  }, [value, setValue]);
  return [value, setValue];
};

export default useLocalStorageProvider;
