import React, { useCallback, useContext, useState } from "react";

const UtilContext = React.createContext({});

//used for global setting
export function UtilProvider({ children }) {
  const [selected, setSelected] = useState("Home");

  const context = {
    selected,
    setSelected,
  };

  return (
    <UtilContext.Provider value={context}>{children}</UtilContext.Provider>
  );
}

export const useUtilProvider = () => {
  return useContext(UtilContext);
};
