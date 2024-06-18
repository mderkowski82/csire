import {ParentMenuItemWithCollapse} from "Frontend/components/parent-item-with-collapse";
import {useEffect, useState} from "react";
import ParentMenuItem from "Frontend/generated/pl/npesystem/services/tables/ParentMenuItem";
import {MenuEndpoint} from "Frontend/generated/endpoints";


export default function AppMenu() {
	const [menuItems, setMenuItems] = useState<ParentMenuItem[]>();

	useEffect(() => {
		async function fetchMenuItems() {
			const menu = await MenuEndpoint.getMenu();
			if(menu) {
				setMenuItems(menu);
			}
		}
		fetchMenuItems();
	}, []);

	if(menuItems === undefined) {
		return <div>Loading...</div>;
	} else return (
		<>
			{menuItems?.map(menuItem => {
				return <ParentMenuItemWithCollapse parentMenuItem={menuItem}/>
			})}
		</>
	);
}