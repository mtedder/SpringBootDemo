/**
 * Created by maurice on 8/21/17.
 * javascript file for all jsp pages
 */
window.onload = function () {
    console.log('Onload.');
    //set up event listener for group dropdown
    var select = document.getElementById("groups");
    select.addEventListener("change", function() {
        console.log(this.value);
        setGroupClasses(this.value);
    });

    console.log(select.value);
    //set hidden field in forms
    setGroupClasses(select.value);
};

/*
 * Set the value of all groupselect classes
 */
function setGroupClasses(val) {
    var groupselectCollection = document.getElementsByClassName("groupselect");
    console.log(groupselectCollection.length);

    for (var i=0; i < groupselectCollection.length; i++) {
        groupselectCollection[i].value = val;
        console.log(groupselectCollection[i].value);
    }
}
