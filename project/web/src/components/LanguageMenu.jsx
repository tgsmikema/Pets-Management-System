import { FormControl, InputLabel, Select, MenuItem } from "@mui/material";
import { useLanguageProvider } from "../providers/LanguageProvider.jsx";

const LanguageMenu = () => {
  const { language, setLanguage } = useLanguageProvider();

  const handleChange = (event) => {
    setLanguage(event.target.value);
  };
  return (
		<FormControl
			sx={{ m: 1, minWidth: 120 }}
			size='small'>
			<Select
				displayEmpty
				inputProps={{ 'aria-label': 'Without label' }}
				defaultValue={language}
				onChange={handleChange}>
				<MenuItem value='English'>
					<em>English</em>
				</MenuItem>
				<MenuItem value={'Maori'}>Māori</MenuItem>
			</Select>
		</FormControl>
  );
};

export default LanguageMenu;
