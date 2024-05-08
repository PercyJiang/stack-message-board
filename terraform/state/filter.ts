import * as TfState from "./tfstate.json";

console.log("percy: start");

const value_list = TfState.values.root_module.resources;
const data_list = value_list.filter((value) => value.mode === "data");
console.log("percy: data_list: ", data_list);
const resource_list = value_list.filter((value) => value.mode === "managed");
console.log("percy: resource_list: ", resource_list);

console.log("percy: end");
