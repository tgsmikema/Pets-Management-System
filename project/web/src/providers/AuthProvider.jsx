import React, { useCallback, useContext, useMemo, useState } from "react";
import axios from "axios";

const AuthContext = React.createContext({});

//used for global user setting
export function AuthProvider({ children }) {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState("");

  const login = useCallback(
    async (userName, passWord) => {
      const res = await axios.get(
        "http://electronicnz-001-site1.ftempurl.com/api/login",
        {
          headers: {
            Authorization: "Basic " + btoa(`${userName}:${passWord}`),
          },
        }
      );
      return res;
    },
    [user, setUser]
  );

  const context = {
    user,
    setUser,
    token,
    setToken,
    login,
  };

  return (
    <AuthContext.Provider value={context}>{children}</AuthContext.Provider>
  );
}

export const useAuth = () => {
  return useContext(AuthContext);
};
