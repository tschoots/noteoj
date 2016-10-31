package main

import (
	"fmt"
	"html/template"
	"net/http"
	"os/exec"
	//"io"
	"encoding/json"
	"os"
	"path/filepath"
	"strings"
)

//type Property struct {
//	Property string `json:"property"`
//	Value    string `json:"value"`
//}
//
//type PropertyList struct {
//	Items []Property `json:"items"`
//}

type PageData struct {
	PName    string
	PValue   string
	Message  string
	PropList PropertyList
}

type PropertyList struct {
	Items []struct {
		Property string `json:"property"`
		Value    string `json:"value"`
	} `json:"items"`
}

func handler(w http.ResponseWriter, r *http.Request) {

	fmt.Printf("req method : %s\n", r.Method)

	// get the total propety list
	// get the prop list
	cmd, err := exec.Command("docker",
		"run",
		"--rm",
		"tschoots/middle_stub",
		"bash",
		"-c",
		"./middle_stub").Output()

	if err != nil {
		fmt.Printf("ERRR in exec.Comand : %s\n", err)
		return
	}

	var propList PropertyList
	jsonString := fmt.Sprintf("%s", cmd)
	fmt.Printf("%s", jsonString)
	if err := json.Unmarshal([]byte(jsonString), &propList); err != nil {
		fmt.Printf("ERROR Unmarshall error : %s\n", err)
	}

	data := &PageData{
		PropList: propList,
	}

	if strings.Compare(r.Method, "GET") == 0 {

		templ := template.Must(template.ParseFiles(filepath.Join("templates", "index.html")))
		templ.Execute(w, data)
	} else {

		r.ParseForm()
		fmt.Printf("pname : %s\n", r.FormValue("pname"))
		fmt.Printf("pvalue : %s\n", r.FormValue("pvalue"))
		fmt.Printf("pmethod : %s\n", r.FormValue("pmethod"))

		//double check if it is a propery change
		//if strings.Compare(a, b)

		//double check if set was done
		// set call to java program.
		//when ok put ok message below or error message below
		cmd := exec.Command("docker",
			"run",
			"--rm",
			"tschoots/middle_stub",
			"bash",
			"-c",
			"./middle_stub")
		//			r.FormValue("pmethod"),
		//			r.FormValue("pname"),
		//			r.FormValue("pvalue"))
		fmt.Println(cmd.Args)

		cmd.Stdout = os.Stdout
		cmd.Stderr = os.Stderr

		//cmd.Stdout = w
		//cmd.Stderr = w

		//go io.Copy(log., os.Stderr)
		err := cmd.Run()
		if err != nil {
			fmt.Println(err.Error())
			//return false, err
		}



        data.PName =  r.FormValue("pname")
        data.PValue = r.FormValue("pvalue")
        data.Message = fmt.Sprintf("property : %s was set to %s\n", data.PName, data.PValue )

		templ := template.Must(template.ParseFiles(filepath.Join("templates", "index.html")))
		templ.Execute(w, data)
	}


}

func main() {
	http.HandleFunc("/", handler)
	if err := http.ListenAndServe(":8080", nil); err != nil {
		fmt.Printf("error : %s\n", err)
	}
}
