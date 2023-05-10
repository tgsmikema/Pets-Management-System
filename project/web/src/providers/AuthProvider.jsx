import React, { useCallback, useContext, useState } from "react";
import axios from "axios";
import useLocalStorageProvider from "./useLocalStorageProvider.jsx";
import { constants } from "../constants.js";

const AuthContext = React.createContext({});

//used for global user setting
export function AuthProvider({ children }) {
  // we will use local storage to store the user and token, when the user log out these will clear
  const [user, setUser] = useLocalStorageProvider("user", null);
  const [loading, setLoading] = useState(false);

  //this is login function, have two parameters username,password, this function will return Promise
  //object, if success, the attribute data will contain the data we want to use
  const login = useCallback(
    async (userName, passWord) => {
      setLoading(true);
      const res = await axios.get(`${constants.backend}/user/login`, {
        headers: {
          Authorization: "Basic " + btoa(`${userName}:${passWord}`),
        },
      });
      setLoading(false);
      setUser(res.data);
    },
    [user]
  );

  //this is logout function, no parameters, just reset user and token as null
  const logout = useCallback(() => {
    setUser(null);
  }, [user]);

  const context = {
    user,
    setUser,
    login,
    logout,
    loading,
  };

  return (
    <AuthContext.Provider value={context}>{children}</AuthContext.Provider>
  );
}

export const useAuth = () => {
  return useContext(AuthContext);
};
