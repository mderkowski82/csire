import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {FuckedPropEndpoint, TableDataProviderEndpoint} from 'Frontend/generated/endpoints.js';
import {useParams, useSearchParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {useAuth} from "Frontend/util/auth";
import {compressData, decompressData} from "Frontend/util/search-params-utils";
import {TableData} from "Frontend/types/table/types";
import Direction from "Frontend/generated/pl/npesystem/models/dto/FilterRequestDTO/Direction";
import FuckedPropInfo from "Frontend/generated/pl/npesystem/models/records/FuckedPropInfo";
import LogicOperator from "Frontend/generated/pl/npesystem/models/dto/FilterRequestDTO/LogicOperator";
import Operation from "Frontend/generated/pl/npesystem/models/dto/FilterRequestDTO/Operation";

export const config: ViewConfig = {
	menu: {order: 66, icon: 'line-awesome/svg/globe-solid.svg'},
	title: 'Table',
};



export default function TableView() {
	const [fuckedPropInfo,setFuckedPropInfo] = useState<FuckedPropInfo>();
	const [fetchData,setFetchData] = useState<FuckedPropInfo>();
	const { clazz } = useParams();
	const {state, logout} = useAuth();
	const [searchParams, setSearchParams] = useSearchParams();
	const compressedData = searchParams.get('table');




	useEffect(() => {
		if(clazz) {
			FuckedPropEndpoint.getFuckedPropInfoByClazzId(clazz).then(response => {
				if(response) setFuckedPropInfo(response);
			})
		}
		if(compressedData) {
			console.log(decompressData(compressedData));
		} else {
			const json:TableData = {
				page: 1,
				pageSize:50,
				sortedBy:'id',
				sortedDirection: "asc",
				clazz: clazz ?? ""
			}
			searchParams.set('table', compressData(json));
			setSearchParams(searchParams);
		}

		TableDataProviderEndpoint.getTableDataFiltered({
			entityName:"aa",
			pageRequest:{
				page: 1,
				size: 10,
				sort: {
					direction: Direction.ASC,
					property: "id"
				}
			},
			filters: [
				{
					fieldName: "name",
					logicOperator: LogicOperator.AND,
					values: ["Emma Executive"],
					operation: Operation.EQUALS,
					wildcard: ""
				}
			]
		}).then(value => setFetchData(value ?? ""));

	}, [clazz]);


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
					{JSON.stringify(fetchData, null, 2)}
				</pre>
				<pre>
					{compressedData && JSON.stringify(decompressData(compressedData), null, 2)}
				</pre>
			</section>
		</>
	);
}
