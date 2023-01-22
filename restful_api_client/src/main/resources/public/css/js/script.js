function login(){
    const loginBtn = document.getElementById("submitBtn");
    loginBtn.addEventListener("submit",(event)=>{
        event.preventDefault();

        const formData = new FormData(event.target);
        const email = formData.get("email");

        const json = {
            "email": email
        }

        const options = {
            method : "POST",
            body: JSON.stringify(json)
        }

        fetch("http://localhost:7070/users")

    })
}

const setUp = () => {
    login();

}
