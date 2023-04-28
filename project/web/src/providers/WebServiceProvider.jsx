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
  //this will use for list centres for different userType
  const [allCentres, setAllCentres] = useState([]);

  //we want to fetch all centres when the app start according to the user type
  useEffect(() => {
    (async () => {
      await handleCentres();
    })();
  }, [user]);

  //handle the centre list, will add the All Centres item for the admin user
  const handleCentres = useCallback(async () => {
    const res = await fetchAllCenters();
    const centers = res.data.map((it) => it.name);
    if (user.userType === "admin") {
      setAllCentres(["All Centres", ...centers]);
    } else {
      const center = res.data.find((it) => it.id === user.centreId);
      setAllCentres([center.name]);
    }
  }, [user]);

  //fetch all centres
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
