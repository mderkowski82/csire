import {useState} from 'react';
import {SideNavItem, Icon} from '@vaadin/react-components';
import ParentMenuItem from "Frontend/generated/pl/npesystem/services/tables/ParentMenuItem";

export default function ParentMenuItemWithCollapse({parentMenuItem}: {parentMenuItem: ParentMenuItem}) {
	const [isOpen, setIsOpen] = useState(false);
	const toggleIsOpen = () => setIsOpen(!isOpen);

	return (
		<>
			<SideNavItem onClick={toggleIsOpen}>
				{isOpen ? <Icon icon="angle-down" /> : <Icon icon="angle-right" />}
				{parentMenuItem.label}
			</SideNavItem>
			{isOpen && parentMenuItem.childs?.map(childMenuItem =>
				<SideNavItem path={`entity/${parentMenuItem.position}/${childMenuItem.position}`} key={childMenuItem.position}>
					{childMenuItem.label}
				</SideNavItem>
			)}
		</>
	);
};