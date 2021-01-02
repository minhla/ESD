/*
Class: login
Description: javascript to show the map
Created: 30/12/2020
Updated: 30/12/2020
Author/s: Giacomo Pellizzari
*/

// Initialize and add the map
function initMap() {
    console.log("Map.js is connected correctly mate");
    
    //get locations passed from servlet
    numberOfLocations = tempLocations.length;
    var locations = [];
    //divide each element of the array to have the models variables
    tempLocations.forEach(function (item, index) {
        console.log(item, index);
        tempArray = item.split(',');
        console.log(tempArray[0]);
        var location = {idNum:tempArray[0], name:tempArray[1], type:tempArray[2], lat:parseFloat(tempArray[3]), lng:parseFloat(tempArray[4])};
        locations.push(location);
    });
    
    
    // The location of Uluru
    const uluru = { lat: 51.45, lng: -2.60 };
    
    
    // The map, centered at Uluru
    const map = new google.maps.Map(document.getElementById("map"), {
      zoom: 10,
      center: uluru,
      gestureHandling: "cooperative",
      zoomControl: false
    });
    
    //show all of the locations
    showLocations();
    
    
    
    
    
    //Listen for any clicks on the map.
    /*google.maps.event.addListener(map, 'click', function(event) {                
        //Get the location that the user clicked.
        var clickedLocation = event.latLng;
        console.log(clickedLocation.lat());
        console.log(clickedLocation.lng());
        
        
        //If the marker hasn't been added.
        if(marker === false){
            //Create the marker.
            marker = new google.maps.Marker({
                position: clickedLocation,
                map: map,
                draggable: true //make it draggable
            });
            //Listen for drag events!
            google.maps.event.addListener(marker, 'dragend', function(event){
                markerLocation();
            });
        } else{
            //Marker has already been added, so just change its location.
            marker.setPosition(clickedLocation);
        }
        //Get the marker's location.
        markerLocation();
    });*/
    
    //This function will get the marker's current location and then add the lat/long
    //values to our textfields so that we can save the location.
    function markerLocation(){
        //Get location.
        var currentLocation = marker.getPosition();
        //Add lat and lng values to a field that we can save.
        document.getElementById('lat').value = currentLocation.lat(); //latitude
        document.getElementById('lng').value = currentLocation.lng(); //longitude
    }
    
    function showLocations(){
        var i;
        for(i=0; i<2; i++){
            console.log("new marker added");
            marker = new google.maps.Marker({
                position: new google.maps.LatLng(locations[i].lat, locations[i].lng),
                map: map,
            });
            
            var infowindow = new google.maps.InfoWindow();
            
            //listen for clicks on the markers
            google.maps.event.addListener(marker, 'click', (function(marker, i) {
                return function() {
                    console.log("clicked");
                    document.getElementById('lat').value = locations[i].lat; //latitude
                    document.getElementById('lng').value = locations[i].lng; //longitude
                    document.getElementById('location').value = locations[i].name;
                    document.getElementById('type').value = locations[i].type;
                }
            })(marker, i));
            
            //listen for hovers on the markers
            google.maps.event.addListener(marker, 'mouseover', (function(marker, i) {
                return function() {
                    console.log("hovered on marker");
                    infowindow.setContent(locations[i].name);
                    infowindow.open(map, marker);
                }
            })(marker, i));
            
            //listen when going away from marker
            google.maps.event.addListener(marker, 'mouseout', (function(marker, i) {
                return function() {
                    console.log("hovered away from marker");
                    infowindow.close();
                }
            })(marker, i));
            
            console.log("added marker");
        }
    }
    
    
}

