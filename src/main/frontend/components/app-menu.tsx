import {useEffect, useState} from "react";
import ParentMenuItem from "Frontend/generated/pl/npesystem/services/tables/ParentMenuItem";
import {MenuEndpoint} from "Frontend/generated/endpoints";
import ParentMenuItemWithCollapse from "Frontend/components/parent-item-with-collapse";


export default function AppMenu() {
	const [menuItems, setMenuItems] = useState<ParentMenuItem[]>();
	const [isLoaded, setIsLoaded] = useState(false);
	useEffect(() => {
		async function fetchMenuItems() {
			const menu = await MenuEndpoint.getMenu();
			if(menu) {
				setMenuItems(menu);
				setIsLoaded(true)
			}
		}
		fetchMenuItems();
	}, []);

	if(!isLoaded) {
		return <div>Loading...</div>;
	} else return (
		<>
			{menuItems?.map(menuItem => {
				return <ParentMenuItemWithCollapse key={menuItem.label} parentMenuItem={menuItem}/>
			})}
		</>
	);
}