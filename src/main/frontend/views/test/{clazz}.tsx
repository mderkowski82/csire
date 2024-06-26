import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {TableDataProviderEndpoint} from 'Frontend/generated/endpoints.js';
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {decompressData} from "Frontend/util/search-params-utils";
import {TableData} from "Frontend/types/table/types";
import Direction from "Frontend/generated/pl/npesystem/models/dto/FilterRequestDTO/Direction";
import {Grid, GridSortColumn} from "@vaadin/react-components";
import {booleanFalseEquals, booleanNullEquals, booleanTrueEquals} from "Frontend/components/testPredicates";

export const config: ViewConfig = {
	menu: {order: 66, icon: 'line-awesome/svg/globe-solid.svg'},
	title: 'Table',
};

export default function TableView() {
	const { clazz } = useParams();

	const[booleanNullEqualsJson, setBooleanNullEqualsJson] = useState<Record<any, any>>([]);
	const[booleanTrueEqualsJson, setBooleanTrueEqualsJson] = useState<Record<any, any>>([]);
	const[booleanFalseEqualsJson, setBooleanFalseEqualsJson] = useState<Record<any, any>>([]);

	const json:TableData = {
		page: 1,
		pageSize:50,
		sortedBy:'id',
		sortedDirection: "asc",
		clazz: clazz ?? ""
	}

	const pageRequest = {
		page: 0,
		size: 10,
		sort: {
			direction: Direction.ASC,
			property: "id"
		}
	}

	useEffect(() => {

		TableDataProviderEndpoint.getTableDataFiltered({
			entityName:"TestEntity",
			pageRequest: pageRequest,
			filters: [
				booleanNullEquals
			]
		}).then(value => {
			if(value) setBooleanNullEqualsJson(value);
		});

		TableDataProviderEndpoint.getTableDataFiltered({
			entityName:"TestEntity",
			pageRequest: pageRequest,
			filters: [
				booleanTrueEquals
			]
		}).then(value => {
			if(value) setBooleanTrueEqualsJson(value);
		});

		TableDataProviderEndpoint.getTableDataFiltered({
			entityName:"TestEntity",
			pageRequest: pageRequest,
			filters: [
				booleanFalseEquals
			]
		}).then(value => {
			if(value) setBooleanFalseEqualsJson(value);
		});

	}, [clazz]);



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
				booleanNullEqualsJson
				<pre>
					{JSON.stringify(booleanNullEqualsJson, null, 2)}
				</pre>

				booleanTrueEqualsJson
				<pre>
					{JSON.stringify(booleanTrueEqualsJson, null, 2)}
				</pre>

				booleanFalseEqualsJson
				<pre>
					{JSON.stringify(booleanFalseEqualsJson, null, 2)}
				</pre>
			</section>
		</>
	);
}
