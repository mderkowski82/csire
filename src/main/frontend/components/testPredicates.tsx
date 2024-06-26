import Operation from "Frontend/generated/pl/npesystem/models/dto/FilterRequestDTO/Operation";

export const booleanNullEquals = {
	fieldName: "aBooleanValue",
	values: [],
	operation: Operation.NULL,
	wildcard: ""
}

export const booleanTrueEquals = {
	fieldName: "aBooleanValue",
	values: [true],
	operation: Operation.EQUALS,
	wildcard: ""
}

export const booleanFalseEquals = {
	fieldName: "aBooleanValue",
	values: [false],
	operation: Operation.EQUALS,
	wildcard: ""
}

export const booleanNullNotEquals = {
	fieldName: "aBooleanValue",
	values: [null],
	operation: Operation.NOT_EQUALS,
	wildcard: ""
}

export const booleanTrueNotEquals = {
	fieldName: "aBooleanValue",
	values: [true],
	operation: Operation.NOT_EQUALS,
	wildcard: ""
}

export const booleanFalseNotEquals = {
	fieldName: "aBooleanValue",
	values: [false],
	operation: Operation.NOT_EQUALS,
	wildcard: ""
}

export const booleanNullIn = {
	fieldName: "aBooleanValue",
	values: [null],
	operation: Operation.IN,
	wildcard: ""
}

export const booleanTrueIn = {
	fieldName: "aBooleanValue",
	values: [true],
	operation: Operation.IN,
	wildcard: ""
}

export const booleanFalseIn = {
	fieldName: "aBooleanValue",
	values: [false],
	operation: Operation.IN,
	wildcard: ""
}

