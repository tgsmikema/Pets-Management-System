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
  const [centreLoading, setCentreLoading] = useState(false);
  const [allCentreForAllUser, setAllCentreForAllUser] = useState([]);

  //we want to fetch all centres when the app start according to the user type
  useEffect(() => {
    (async () => {
      setCentreLoading(true);
      await handleCentres();
      setCentreLoading(false);
    })();
  }, [user]);

  //handle the centre list, will add the All Centres item for the admin user
  const handleCentres = useCallback(async () => {
    const res = await fetchAllCenters();
    const centers = res?.map((it) => it.name);
    if (user?.userType === "admin") {
      setAllCentres(["All Centres", ...centers]);
    } else {
      const center = res?.find((it) => it.id === user?.centreId);
      setAllCentres([center?.name]);
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
      setAllCentreForAllUser(centers?.data);
      return centers?.data;
    }
  }, [user]);

  const value = {
    allCentres,
    handleCentres,
    setAllCentres,
    centreLoading,
    allCentreForAllUser,
  };

  return <WebContext.Provider value={value}>{children}</WebContext.Provider>;
}

export const useWebService = () => useContext(WebContext);
