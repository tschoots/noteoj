package main

import (
	"encoding/json"
	"fmt"
	"os"
)

type Property struct {
	Property string `json:"property"`
	Value    string `json:"value"`
}

type PropertyList struct {
	Items []Property `json:"items"`
}

func main() {

	one := Property{
		Property: "prop1",
		Value:    "value1",
	}

	two := Property{
		Property: "prop2",
		Value:    "value2",
	}

	three := Property{
		Property: "prop3",
		Value:    "value3",
	}

	props := []Property{
		one,
		two,
		three,
	}

	data := &PropertyList{
		Items: props,
	}

	jsonReport, err := json.MarshalIndent(data, "", "   ")
	if err != nil {
		fmt.Printf("ERROR marshall of report went wrong : \n%s\n", err)
		os.Exit(1)
	}

	fmt.Printf("%s\n\n", jsonReport)

}
