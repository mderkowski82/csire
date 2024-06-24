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
import {Grid, GridSortColumn} from "@vaadin/react-components";

export const config: ViewConfig = {
	menu: {order: 66, icon: 'line-awesome/svg/globe-solid.svg'},
	title: 'Table',
};



export default function TableView() {
	const [fuckedPropInfo,setFuckedPropInfo] = useState<FuckedPropInfo>();
	const [fetchData,setFetchData] = useState<Record<string,any>>();
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
			entityName:"TestEntity",
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
					fieldName: "longValue",
					logicOperator: LogicOperator.AND,
					values: [5],
					operation: Operation.EQUALS,
					wildcard: ""
				}
			]
		}).then(value => {
			console.log(value)
			setFetchData(value)
		});
	}, [clazz]);


	const userRoles = state.user?.roles;
	const viewRoleRequired = fuckedPropInfo?.view;
	const editRoleRequired = fuckedPropInfo?.edit;

	const canView = userRoles?.some(role => viewRoleRequired?.includes(role));
	const canEdit = userRoles?.some(role => editRoleRequired?.includes(role));


	function formatElement(itemElement: any): string {
		if(typeof itemElement === 'object' && 'id' in itemElement && 'value' in itemElement) {
			return `[${itemElement.id}] ${itemElement.value}`
		} else {
			return itemElement;
		}
	}

	function getFormatted(itemElement: any) : string {
		if (typeof itemElement === 'object') {
			if (Array.isArray(itemElement)) {
				return itemElement.map(element => formatElement(element)).join(', ')
			}
			else {
				return formatElement(itemElement);
			}
		}
		return itemElement;
	}
	return (
		<>
			<section className="flex flex-col p-m gap-m">
				<div>View: {canView ? "TAK" : "NIE"}</div>
				<div>Edit: {canEdit ? "TAK" : "NIE"}</div>
				<div>
					<Grid items={fetchData as [] ?? []}>
						{
							fuckedPropInfo?.defaultColumn.map(value => {
								return <GridSortColumn key={value.fieldName} header={value.label} path={value.fieldName}>
									{props => getFormatted(props.item[value.fieldName])}
								</GridSortColumn>
							})
						}
					</Grid>
				</div>
				<pre>
					{JSON.stringify(fuckedPropInfo, null, 2)}
				</pre>
				<pre>
					{JSON.stringify(fetchData, null,2)}
				</pre>
				<pre>
					{compressedData && JSON.stringify(decompressData(compressedData), null, 2)}
				</pre>
			</section>
		</>
	);
}
