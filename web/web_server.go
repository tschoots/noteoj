package main

import (
	"fmt"
	"net/http"
	//"os/exec"
	"html/template"
	//"io"
	//"os"
	"path/filepath"
	"strings"
)

type PageData struct {
	PName  string
	PValue string
	Message string
	
}

type PropertyList {
	Items  [] struct {
		Property  string `json:"property"`
		Value     string `json:"value"`
	} `json:"items"`
}

func handler(w http.ResponseWriter, r *http.Request) {

	fmt.Printf("req method : %s\n", r.Method)

	// get the total propety list
	
	if strings.Compare(r.Method, "GET") == 0 {
		data := &PageData{
			
		}
		templ := template.Must(template.ParseFiles(filepath.Join("templates", "index.html")))
		templ.Execute(w, data)
	} else {
		
		r.ParseForm()
		fmt.Printf("pname : %s\n", r.FormValue("pname"))
		fmt.Printf("pvalue : %s\n", r.FormValue("pvalue"))
		fmt.Printf("pmethod : %s\n", r.FormValue("pmethod"))
		
		data := &PageData{
			PName:  r.FormValue("pname"),
			PValue: r.FormValue("pvalue"),
			Message: "yes",
		}

		templ := template.Must(template.ParseFiles(filepath.Join("templates", "index.html")))
		templ.Execute(w, data)
	}

	//cmd := exec.Command("java",
	//                    "-version")
	//fmt.Println(cmd.Args)
	//cmd.Stdout = os.Stdout
	//cmd.Stderr = os.Stderr

	//cmd.Stdout = w
	//cmd.Stderr = w

	//go io.Copy(log., os.Stderr)
	//err := cmd.Run()
	//if err != nil {
	//	fmt.Println(err.Error())
	//	//return false, err
	//}
}

func main() {
	http.HandleFunc("/", handler)
	if err := http.ListenAndServe(":8080", nil); err != nil {
		fmt.Printf("error : %s\n", err)
	}
}
