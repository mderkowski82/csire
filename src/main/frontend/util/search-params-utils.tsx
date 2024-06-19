import pako from 'pako';



export function getSearchParam(searchParam: string): Record<string, string> {
	const searchParams = new URLSearchParams(searchParam);
	const result: Record<string, string> = {};
	for (const [key, value] of searchParams) {
		result[key] = value;
	}
	return result;
}



export function compressData(data: Record<string, any>): string {
	const jsonString = JSON.stringify(data);
	const compressed = pako.gzip(jsonString);
	return btoa(String.fromCharCode.apply(null, compressed as unknown as number[]));
}

export function decompressData(compressedData: string): Record<string, any> {
	try {
		const binaryString = window.atob(compressedData);
		const binaryChar = [...binaryString].map((char) => char.charCodeAt(0));
		const decompressed = pako.ungzip(new Uint8Array(binaryChar), { to: 'string' });
		return JSON.parse(decompressed);
	} catch (e) {
		return {}
	}

}