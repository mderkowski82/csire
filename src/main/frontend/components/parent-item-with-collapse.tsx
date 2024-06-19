import {useState} from 'react';
import {HorizontalLayout, SideNavItem} from '@vaadin/react-components';
import {ChevronDown, ChevronUp} from "lucide-react";
import ParentMenuItem from "Frontend/generated/pl/npesystem/services/records/ParentMenuItem";

export default function ParentMenuItemWithCollapse({parentMenuItem}: { parentMenuItem: ParentMenuItem }) {
	const [isOpen, setIsOpen] = useState(false);
	const toggleIsOpen = () => setIsOpen(!isOpen);

	return (
		<div className={"w-full border-b"}>
			<SideNavItem onClick={toggleIsOpen}>
				<HorizontalLayout  className={"flex justify-between items-center"}>
					{parentMenuItem.label}
					{isOpen ? <ChevronUp/> : <ChevronDown/>}
				</HorizontalLayout>
			</SideNavItem>
			{isOpen && parentMenuItem.childs?.map(childMenuItem =>
				<SideNavItem path={`entity/${childMenuItem.entity.clazz}`} key={childMenuItem.position}>
					{childMenuItem.label}
				</SideNavItem>
			)}
		</div>
	);
};