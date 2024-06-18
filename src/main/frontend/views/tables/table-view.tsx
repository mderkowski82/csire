import {ViewConfig} from '@vaadin/hilla-file-router/types.js';
import {useSignal} from '@vaadin/hilla-react-signals';
import {Button} from '@vaadin/react-components/Button.js';
import {Notification} from '@vaadin/react-components/Notification.js';
import {TextField} from '@vaadin/react-components/TextField.js';
import {FuckedPropEndpoint, HelloWorldService} from 'Frontend/generated/endpoints.js';

export const config: ViewConfig = {
	menu: {order: 66, icon: 'line-awesome/svg/globe-solid.svg'},
	title: 'Table',
};

export default function TableView() {
	const name = useSignal('');

	return (
		<>
			<section className="flex flex-col p-m gap-m">
				<Button
					onClick={async () => {
						const serverResponse = await FuckedPropEndpoint.getTableEntities();
						name.value = JSON.stringify(serverResponse, null, 2)
						Notification.show(JSON.stringify(serverResponse, null, 2));
					}}
				>
					GET
				</Button>
				<pre>
					{name.value}
				</pre>
			</section>
		</>
	);
}
