import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {useSignal} from '@vaadin/hilla-react-signals';
import {Button} from '@vaadin/react-components/Button.js';
import {Notification} from '@vaadin/react-components/Notification.js';
import {TextField} from '@vaadin/react-components/TextField.js';
import {FuckedPropEndpoint} from 'Frontend/generated/endpoints.js';
import {useParams} from "react-router-dom";
import FuckedPropInfo from "Frontend/generated/pl/npesystem/services/records/FuckedPropInfo";
import {useEffect, useState} from "react";
import {useAuth} from "Frontend/util/auth";
import {getSearchParam} from "Frontend/util/search-params-utils";

export const config: ViewConfig = {
	menu: {order: 66, icon: 'line-awesome/svg/globe-solid.svg'},
	title: 'Table',
};

export default function TableView() {
	const [fuckedPropInfo,setFuckedPropInfo] = useState<FuckedPropInfo>();
	const { clazz } = useParams();
	const {state, logout} = useAuth();

	const searchParam = getSearchParam(window.location.search);



	useEffect(() => {
		if(clazz) {
			FuckedPropEndpoint.getFuckedPropInfoByClazzId(clazz).then(response => {
				if(response) setFuckedPropInfo(response);
			})
		}
	}, []);


	const userRoles = state.user?.roles;
	const viewRoleRequired = fuckedPropInfo?.view;
	const editRoleRequired = fuckedPropInfo?.edit;

	const canView = userRoles?.some(role => viewRoleRequired?.includes(role));
	const canEdit = userRoles?.some(role => editRoleRequired?.includes(role));


	return (
		<>
			<section className="flex flex-col p-m gap-m">
				<div>View: {canView ? "TAK" : "NIE"}</div>
				<div>Edit: {canEdit ? "TAK" : "NIE"}</div>
				<div></div>
				<pre>
					{JSON.stringify(fuckedPropInfo, null, 2)}
				</pre>
				<pre>
					{JSON.stringify(searchParam, null, 2)}
				</pre>
			</section>
		</>
	);
}
