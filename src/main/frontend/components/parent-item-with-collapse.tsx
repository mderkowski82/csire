import {useState} from 'react';
import {HorizontalLayout, Icon, SideNavItem} from '@vaadin/react-components';
import ParentMenuItem from "Frontend/generated/pl/npesystem/services/tables/ParentMenuItem";
import {ArrowDown, ArrowUp, ChevronDown, ChevronUp, LineChart, LucideSeparatorHorizontal, SeparatorHorizontal, Spline} from "lucide-react";

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
				<SideNavItem path={`entity/${parentMenuItem.position}/${childMenuItem.position}`} key={childMenuItem.position}>
					{childMenuItem.label}
				</SideNavItem>
			)}
		</div>
	);
};