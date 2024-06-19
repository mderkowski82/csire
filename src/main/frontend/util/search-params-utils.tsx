import JSZip from 'jszip';


export function getSearchParam(searchParam: string): Record<string, string> {
	const searchParams = new URLSearchParams(searchParam);
	const result: Record<string, string> = {};
	for (const [key, value] of searchParams) {
		result[key] = value;
	}
	return result;
}


function compressToZip(data: Record<string, any>): Promise<string> {
	const zip = new JSZip();
	zip.file('data.json', JSON.stringify(data));
	return zip.generateAsync({ type: 'base64' });
}

async function decompressFromZip(base64Data: string): Promise<Record<string, any>> {
	const zip = await JSZip.loadAsync(base64Data, { base64: true });
	const jsonData = await zip.file('data.json')?.async('text');
	if(jsonData) {
		return JSON.parse(jsonData);
	} else {
		return {}
	}
}