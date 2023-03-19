import React, {useContext, useState} from "react";

const AuthContext = React.createContext({})

//used for global user setting
export function AuthProvider({children}) {

    const [user,setUser] = useState(null);

    const context = {
        user,
        setUser
    }

    return (
        <AuthContext.Provider value={context}>
            {children}
        </AuthContext.Provider>
    )
}

export const useAuth = () =>{
     return useContext(AuthContext);
}