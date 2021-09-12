function toggleDisplay(card,display){
    if(card.style.display == 'none'){
        card.style.display = 'block'
    } else
    {
        card.style.display = 'none';
    }
}

// the card shown when clicking a date
function setDate(button, date){
    debugger;
    button.addEventListener('click', function(){
        var cards = document.getElementsByClassName(date);
        var noEvents = document.getElementById('empty');
        var eventCards = document.getElementsByClassName('events');
        if(cards.length != 0){
            //hide the no events card
            noEvents.style.display = 'none';
            for(i=0;i<eventCards.length;i++){
                eventCards[i].style.display = 'none';
            }
            for(i=0;i<cards.length;i++){
                cards[i].style.display = 'block';
            }
        }
        else{
            for(i=0;i<eventCards.length;i++){
                eventCards[i].style.display = 'none';
            }
            var noEvents = document.getElementById('empty');
            noEvents.style.display = 'block';
        }
    });
}

function closeCard(button){
    button.parentNode.parentNode.style.display = 'none';
}


const monthNames = ["January", "February", "March", "April", "May", "June",
    "July", "August", "September", "October", "November", "December"
];

const days = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];


var year= 2021;
var month = 8; //starts counting from 0
var day = 1;

var getDaysInMonth = function(month,year) {
    debugger;
    // Here January is 1 based
    //Day 0 is the last day in the previous month
    return new Date(year, month, 0).getDate();
    // Here January is 0 based
    // return new Date(year, month+1, 0).getDate();
};



// THE CALENDAR
var calendarDiv = document.getElementById("days");
var today = new Date();

/**
 * 2 - MARCH , starts counting from 0
 * */

function populateList(daysCurrentMonth){
    debugger;
    for(i=0;i<daysCurrentMonth;i++){
        if(i<10 && date.getMonth()<10 ){
            var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

        } else if(i<10){
            var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
        }
        else if(date.getMonth()<10){
            var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

        }
        else{
            var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
        }

        var link = document.createElement("a");

        setDate(link,stringDate);

        var item = document.createElement("li");
        // item.classList.add('class'+(date.getMonth()+1).toString());
        item.classList.add('date');

        if(i==today.getDate()-1 && today.getMonth()==date.getMonth()){
            item.style.background = '#1abc9c';
            item.style.color = 'whitesmoke';
            item.style.padding = '5px';
        }

        var card = document.getElementsByClassName(stringDate);

        if(card.length != 0){
            item.style.background = '#07eb9b';
            item.style.color = 'white';
            item.style.padding = '5px';
        }
        item.innerText = (i+1).toString();

        link.appendChild(item);
        calendarDiv.appendChild(link);
    }
}


function populateCalendar(month, year, day, today){
    debugger;
    today = new Date();
    date = new Date(year, month, day, 0 , 0, 0, 0) // the current month

    document.getElementById("month").innerText = monthNames[date.getMonth()];
    document.getElementById("currentYear").innerText = date.getFullYear();

    //1 -based, March id 3rd
    var daysCurrentMonth = getDaysInMonth(date.getMonth()+1, year);
    var firstDay = new Date(date.getFullYear(), date.getMonth(), 1);

    if(days[firstDay.getDay()] === 'Monday' ){
        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    } else if(days[firstDay.getDay()] === 'Tuesday' ){

        //add one empty li
        var link = document.createElement("a");
        var item = document.createElement("li");

        item.classList.add('date');
        link.appendChild(item);
        calendarDiv.appendChild(link);

        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    } else if(days[firstDay.getDay()] === 'Wednesday' ){
        //add 2 empty li
        for(i=0;i<2;i++){
            var link = document.createElement("a");
            var item = document.createElement("li");

            item.classList.add('date');
            link.appendChild(item);
            calendarDiv.appendChild(link);
        }
        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    } else if(days[firstDay.getDay()] === 'Thursday' ){
        //add 3 empty li
        for(i=0;i<3;i++){
            var link = document.createElement("a");
            var item = document.createElement("li");

            item.classList.add('date');
            link.appendChild(item);
            calendarDiv.appendChild(link);
        }
        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    } else if(days[firstDay.getDay()] === 'Friday' ){
        //add 4 empty li
        for(i=0;i<4;i++){
            var link = document.createElement("a");
            var item = document.createElement("li");

            item.classList.add('date');
            link.appendChild(item);
            calendarDiv.appendChild(link);
        }
        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    }
    else if(days[firstDay.getDay()] === 'Saturday' ){
        //add 5 empty li
        for(i=0;i<5;i++){
            var link = document.createElement("a");
            var item = document.createElement("li");

            item.classList.add('date');
            link.appendChild(item);
            calendarDiv.appendChild(link);
        }
        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    } else if(days[firstDay.getDay()] === 'Sunday' ){
        //add 6 empty li
        for(i=0;i<6;i++){
            var link = document.createElement("a");
            var item = document.createElement("li");

            item.classList.add('date');
            link.appendChild(item);
            calendarDiv.appendChild(link);
        }
        populateList(daysCurrentMonth);
        return daysCurrentMonth - 28;
    } /*else if(days[firstDay.getDay()] === 'Monday' ){
            console.log('The day is Monday')
            for(i=0;i<daysCurrentMonth;i++){
                if(i<10 && date.getMonth()<10 ){
                    var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

                } else if(i<10){
                    var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
                }
                else if(date.getMonth()<10){
                    var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

                }
                else{
                    var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
                }
                console.log(stringDate);
                var link = document.createElement("a");

                setDate(link,stringDate);

                var item = document.createElement("li");
                // item.classList.add('class'+(date.getMonth()+1).toString());
                item.classList.add('date');
                console.log('The days in this month have class ' + item.classList);
                if(i==today.getDay()-1){
                    item.style.background = '#1abc9c';
                    item.style.color = 'whitesmoke';
                    item.style.padding = '5px';
                }
                var card = document.getElementById(stringDate);
                if(card != null){
                    item.style.background = '#07eb9b';
                    item.style.color = 'white';
                    item.style.padding = '5px';
                }
                item.innerText = (i+1).toString();

                link.appendChild(item);
                calendarDiv.appendChild(link);
            }
            console.log('The current month has ' + daysCurrentMonth);
            return daysCurrentMonth - 28;
        } else if(days[firstDay.getDay()] === 'Monday' ){
            console.log('The day is Monday')
            for(i=0;i<daysCurrentMonth;i++){
                if(i<10 && date.getMonth()<10 ){
                    var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

                } else if(i<10){
                    var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
                }
                else if(date.getMonth()<10){
                    var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

                }
                else{
                    var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
                }
                console.log(stringDate);
                var link = document.createElement("a");

                setDate(link,stringDate);

                var item = document.createElement("li");
                // item.classList.add('class'+(date.getMonth()+1).toString());
                item.classList.add('date');
                console.log('The days in this month have class ' + item.classList);
                if(i==today.getDay()-1){
                    item.style.background = '#1abc9c';
                    item.style.color = 'whitesmoke';
                    item.style.padding = '5px';
                }
                var card = document.getElementById(stringDate);
                if(card != null){
                    item.style.background = '#07eb9b';
                    item.style.color = 'white';
                    item.style.padding = '5px';
                }
                item.innerText = (i+1).toString();

                link.appendChild(item);
                calendarDiv.appendChild(link);
            }
            console.log('The current month has ' + daysCurrentMonth);
            return daysCurrentMonth - 28;
        } else if(days[firstDay.getDay()] === 'Monday' ){
            console.log('The day is Monday')
            for(i=0;i<daysCurrentMonth;i++){
                if(i<10 && date.getMonth()<10 ){
                    var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

                } else if(i<10){
                    var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
                }
                else if(date.getMonth()<10){
                    var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

                }
                else{
                    var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
                }
                console.log(stringDate);
                var link = document.createElement("a");

                setDate(link,stringDate);

                var item = document.createElement("li");
                // item.classList.add('class'+(date.getMonth()+1).toString());
                item.classList.add('date');
                console.log('The days in this month have class ' + item.classList);
                if(i==today.getDay()-1){
                    item.style.background = '#1abc9c';
                    item.style.color = 'whitesmoke';
                    item.style.padding = '5px';
                }
                var card = document.getElementById(stringDate);
                if(card != null){
                    item.style.background = '#07eb9b';
                    item.style.color = 'white';
                    item.style.padding = '5px';
                }
                item.innerText = (i+1).toString();

                link.appendChild(item);
                calendarDiv.appendChild(link);
            }
            console.log('The current month has ' + daysCurrentMonth);
            return daysCurrentMonth - 28;
        } else if(days[firstDay.getDay()] === 'Monday' ){
            console.log('The day is Monday')
            for(i=0;i<daysCurrentMonth;i++){
                if(i<10 && date.getMonth()<10 ){
                    var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

                } else if(i<10){
                    var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
                }
                else if(date.getMonth()<10){
                    var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

                }
                else{
                    var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
                }
                console.log(stringDate);
                var link = document.createElement("a");

                setDate(link,stringDate);

                var item = document.createElement("li");
                // item.classList.add('class'+(date.getMonth()+1).toString());
                item.classList.add('date');
                console.log('The days in this month have class ' + item.classList);
                if(i==today.getDay()-1){
                    item.style.background = '#1abc9c';
                    item.style.color = 'whitesmoke';
                    item.style.padding = '5px';
                }
                var card = document.getElementById(stringDate);
                if(card != null){
                    item.style.background = '#07eb9b';
                    item.style.color = 'white';
                    item.style.padding = '5px';
                }
                item.innerText = (i+1).toString();

                link.appendChild(item);
                calendarDiv.appendChild(link);
            }
            console.log('The current month has ' + daysCurrentMonth);
            return daysCurrentMonth - 28;
        } else if(days[firstDay.getDay()] === 'Monday' ){
            console.log('The day is Monday')
            for(i=0;i<daysCurrentMonth;i++){
                if(i<10 && date.getMonth()<10 ){
                    var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

                } else if(i<10){
                    var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
                }
                else if(date.getMonth()<10){
                    var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

                }
                else{
                    var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
                }
                console.log(stringDate);
                var link = document.createElement("a");

                setDate(link,stringDate);

                var item = document.createElement("li");
                // item.classList.add('class'+(date.getMonth()+1).toString());
                item.classList.add('date');
                console.log('The days in this month have class ' + item.classList);
                if(i==today.getDay()-1){
                    item.style.background = '#1abc9c';
                    item.style.color = 'whitesmoke';
                    item.style.padding = '5px';
                }
                var card = document.getElementById(stringDate);
                if(card != null){
                    item.style.background = '#07eb9b';
                    item.style.color = 'white';
                    item.style.padding = '5px';
                }
                item.innerText = (i+1).toString();

                link.appendChild(item);
                calendarDiv.appendChild(link);
            }
            console.log('The current month has ' + daysCurrentMonth);
            return daysCurrentMonth - 28;
        } else if(days[firstDay.getDay()] === 'Monday' ){
            console.log('The day is Monday')
            for(i=0;i<daysCurrentMonth;i++){
                if(i<10 && date.getMonth()<10 ){
                    var stringDate = date.getFullYear() + '-' + 0 +  (date.getMonth()+1)  + '-' + '0' + (i+1).toString();

                } else if(i<10){
                    var stringDate = date.getFullYear() + '-' +  (date.getMonth()+1)  + '-' + 0 + (i+1).toString() ;
                }
                else if(date.getMonth()<10){
                    var stringDate = date.getFullYear() + '-'  + '0'  + (date.getMonth()+1)   + '-' + (i+1).toString() ;

                }
                else{
                    var stringDate = date.getFullYear() + '-' + (date.getMonth()+1)   + '-' + (i+1).toString();
                }
                console.log(stringDate);
                var link = document.createElement("a");

                setDate(link,stringDate);

                var item = document.createElement("li");
                // item.classList.add('class'+(date.getMonth()+1).toString());
                item.classList.add('date');
                console.log('The days in this month have class ' + item.classList);
                if(i==today.getDay()-1){
                    item.style.background = '#1abc9c';
                    item.style.color = 'whitesmoke';
                    item.style.padding = '5px';
                }
                var card = document.getElementById(stringDate);
                if(card != null){
                    item.style.background = '#07eb9b';
                    item.style.color = 'white';
                    item.style.padding = '5px';
                }
                item.innerText = (i+1).toString();

                link.appendChild(item);
                calendarDiv.appendChild(link);
            }
            console.log('The current month has ' + daysCurrentMonth);
            return daysCurrentMonth - 28;
        } */
}

remainingDays = []

// a function that will calculate how many days are remaining from the previous month and the numbers

populateCalendar(month, year, day,today);

// Make the Close button close the card

// handle the days left till the end of month

var calendarDiv = document.getElementById('days');

prevBtn = document.getElementById("previuos");
prevBtn.addEventListener('click', function(){
    calendarDiv.textContent = '';
    month = month -1;
    populateCalendar(month,year,day, today);
});

nextBtn = document.getElementById('next');
nextBtn.addEventListener('click', function(){
    calendarDiv.textContent = '';
    month = month +1;
    populateCalendar(month,year,day, today);
});
