import { createContext, useContext, useState } from "react";

const LanguageContext = createContext({});

export function LanguageProvider({ children }) {
  const [language, setLanguage] = useState("English");

  const languageMap =
    language === "English"
      ? {
          Home: "Home",
          Stats: "Stats",
          Chat: "Chat",
          Profile: "Profile",
          Monday: "Monday",
          Tuesday: "Tuesday",
          Wednesday: "Wednesday",
          Thursday: "Thursday",
          Friday: "Friday",
          Saturday: "Saturday",
          Sunday: "Sunday",
          Week: "Week",
          Month: "Month",
          ThisWeek: "This week",
          Weighed: "Weighed",
          Unweighed: "Unweighed",
          Date: "Date",
          Count: "Count",
          NewMessage: "New Message",
          Flagged: "Flagged",
          Alert: "Alert",
          ID: "ID",
          Name: "Name",
          Breed: "Breed",
          LastCheckIn: "Last check-in",
          Weight: "Weight",
          View: "View",
          January: "January",
          February: "February",
          March: "March",
          April: "April",
          May: "May",
          Email: "Email",
          AccessLevel: "Access Level",
          ChangePassword: "Change password",
          Logout: "Logout",
          NewDog: "New Dog",
          location: "location",
          Cancel: "Cancel",
          Create: "Create",
          Delete: "Delete",
          Save: "Save",
          EditDog: "Edit dog",
          AddWeight: "Add weight",
        }
      : {
          Home: "Haukāinga",
          Stats: "Tatauranga",
          Chat: "Kōrero",
          Profile: "āhau",
          Monday: "Rāhina",
          Tuesday: "Rātū",
          Wednesday: "Rāapa",
          Thursday: "Rāpare",
          Friday: "Rāmere",
          Saturday: "Rāhoroi",
          Sunday: "Rātapu",
          Week: "Wiki",
          Month: "Marama",
          ThisWeek: "Tēnei wiki",
          Weighed: "Pāunatia",
          Unweighed: "Kaua pāunatia",
          Date: "Rā",
          Count: "Tau",
          NewMessage: "Hou kōrero",
          Flagged: "Tūpato",
          Alert: "Ohiti",
          Name: "Ingoa",
          Breed: "Momo",
          LastCheckIn: "Kitea whakamutunga",
          Weight: "Ine taumaha",
          View: "Maea",
          January: "Kohitātea",
          February: "Hui-tanguru",
          March: "Poutū-te-rangi",
          April: "Paenga-whāwhā",
          May: "Haratua",
          Email: "īmēra",
          AccessLevel: "āhei",
          ChangePassword: "Huri kupuhipa",
          Logout: "Takiputa",
          NewDog: "Hou kurī",
          location: "Tauwāhi",
          Cancel: "Whakakore",
          Create: "Hanga",
          Delete: "Muku",
          Save: "Penapena",
          EditDog: "Whakarerekē kurī",
          AddWeight: "Tāpiri taumaha",
        };

  const data = {
    language,
    setLanguage,
    languageMap,
  };

  return (
    <LanguageContext.Provider value={data}>{children}</LanguageContext.Provider>
  );
}

export const useLanguageProvider = () => useContext(LanguageContext);
