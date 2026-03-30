// Sample data for students using for testing our functions
const students = [
{
 name: "Lalit",
 marks: [
   { subject: "Math", score: 78 },
   { subject: "English", score: 82 },
   { subject: "Science", score: 74 },
   { subject: "History", score: 69 },
   { subject: "Computer", score: 88 }
 ],
 attendance: 82
},
{
 name: "Rahul",
 marks: [
   { subject: "Math", score: 90 },
   { subject: "English", score: 85 },
   { subject: "Science", score: 80 },
   { subject: "History", score: 76 },
   { subject: "Computer", score: 92 }
 ],
 attendance: 91
},

{
  name: "Aman",
  marks: [
    { subject: "Math", score: 35 },
    { subject: "English", score: 60 },
    { subject: "Science", score: 55 },
    { subject: "History", score: 65 },
    { subject: "Computer", score: 70 }
  ],
  attendance: 80
}
];

// here I am calculating total marks for a student because I want to manage function independently and I can reuse this function in other places if needed
function getTotal(student) {
  let total = 0;
  student.marks.forEach(mark => total += mark.score);
  return total;
}

// here I am calculating total marks for each student
function calculateTotalMarks(students) {
  students.forEach((student) => {
    // here i Used getTotal function to calculate total marks for each student
    let total = getTotal(student);
    student.totalMarks = total;
    
    student.totalMarks = total;
    // printing total marks in console
    console.log(`${student.name} Total Marks: ${total}`);
  });
}

console.log("1. Total marks for each student");
// calling function because we want to see total marks for each student in console
calculateTotalMarks(students);

// here I am calculating Average marks for each student
function calculateAverage(students) {
  students.forEach((student) => {
    let avg = getTotal(student) / student.marks.length;
    //student.marks.length gives the total number of subjects
    student.average = avg;
    console.log(`${student.name} average: ${student.average.toFixed(1)}`);
    //used toFixed(1) to get 1 digit after the decimal point
  });
}

console.log("2. Average marks for each student");
calculateAverage(students);

// here I am calculating subject-wise highest score in the class
function subjectWiseHighest(students) {
  const highestMarks = {};
  students.forEach((student) => {
    student.marks.forEach((subject) => {
      //if the subject is never seen or if the current score is greater than the previous value
      if (
        highestMarks[subject.subject] === undefined ||
        highestMarks[subject.subject].marks < subject.score
      )
        // highestMarks is an object containing arr
        highestMarks[subject.subject] = {
          name: student.name,
          marks: subject.score,
        };
    });
  });
  return highestMarks;
}
console.log("3. Subject-wise Highest Score in the Class");
const subjectTopper = subjectWiseHighest(students);

//I use for in loop for objects
for (let sub in subjectTopper) {
  console.log(
    `Highest in ${sub} : ${subjectTopper[sub].name} (${subjectTopper[sub].marks})`,
  );
}

// here I am calculating Subject-wise average score in the class
function subjectWiseAverageScore(students) {
  const subTotal = {};
  students.forEach((student) => {
    student.marks.forEach((subject) => {
      //if there is no entry of a subject
      if (subTotal[subject.subject] === undefined) {
        subTotal[subject.subject] = subject.score;
      } else {
        subTotal[subject.subject] += subject.score;
      }
    });
  });
  return subTotal;
}
console.log("4. Subject-wise Average Score");
let subjectTotalAvg = subjectWiseAverageScore(students); //storing the obj returned by our function
let numberOfStudents = students.length;
//for console output
for (let sub in subjectTotalAvg) {
  let avg = subjectTotalAvg[sub] / numberOfStudents;
  console.log(`Average ${sub} Score: ${avg.toFixed(1)}`);
}

// here I am calculating  Overall Class Topper
function getTopper(students) {
  let toppers = []; //using array to store the name and total marks of the toppers
  let maxMark = -1;
  students.forEach((student) => {
   let total = getTotal(student); 

    if (total > maxMark) {
      maxMark = total;
      toppers = [];
      toppers.push({
        name: student.name,
        marks: total,
      });
    } else if (total === maxMark) {
      toppers.push({
        name: student.name,
        marks: total,
      });
    }
  });
  return toppers;
}
console.log("5. Determine Overall Class Topper");
const topperStudents = getTopper(students); //storing the array returned by our function
topperStudents.forEach((entry) => {
  console.log(`Class topper : ${entry.name} with ${entry.marks} marks`);
});


function getGrade(students) {
  students.forEach((student) => {
    let grade = "";

    // storing subjects where student scored less than passing marks
    let failedSubjects = [];

    student.marks.forEach((subject) => {
      if (subject.score <= 40) {
        failedSubjects.push(subject.subject);
      }
    });

    // saving failed subjects inside student object for later use
    student.failedIn = failedSubjects;

    // checking if student has failed in any subject
    if (failedSubjects.length > 0) {
      grade = `fail (failed in ${failedSubjects})`;

    // checking attendance condition
    } else if (student.attendance < 75) {
      grade = "fail (low attendance)";

    } else {
      let avg = getTotal(student) / student.marks.length; // ✅ FIX

      if (avg >= 85) {
        grade = "A";
      } else if (avg >= 70) {
        grade = "B";
      } else if (avg >= 50) {
        grade = "C";
      } else {
        grade = "fail";
      }
    }

    // storing final grade inside student object
    student.grade = grade;

    // printing final grade for each student
    console.log(`${student.name} Grade: ${student.grade}`);
  });
}

console.log("6. Grade for each student");
getGrade(students);