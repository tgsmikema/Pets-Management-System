import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react";
import { useAuth } from "./AuthProvider.jsx";
import axios from "axios";
import { constants } from "../constants.js";

const WebContext = createContext({});

export function WebServiceProvider({ children }) {
  const { user } = useAuth();
  const [allCentres, setAllCentres] = useState([]);

  useEffect(() => {
    (async () => {
      await handleCentres();
    })();
  }, [user]);

  const handleCentres = useCallback(async () => {
    const res = await fetchAllCenters();
    const centers = res.data.map((it) => it.name);
    if (user.userType === "admin") {
      setAllCentres(["All Centres", ...centers]);
    } else {
      const center = res.data.find((it) => it.id === user.centreId);
      setAllCentres([center.name]);
    }
    return allCentres;
  }, [user]);

  const fetchAllCenters = useCallback(async () => {
    if (user !== null) {
      const centers = await axios.get(
        `${constants.backend}/util/listAllCentres`,
        {
          headers: {
            Authorization: "Basic " + user.token,
          },
        }
      );
      return centers;
    }
  }, [user]);

  const value = {
    allCentres,
    handleCentres,
    setAllCentres,
  };

  return <WebContext.Provider value={value}>{children}</WebContext.Provider>;
}

export const useWebService = () => useContext(WebContext);
