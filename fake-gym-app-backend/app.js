const express = require("express");
const bodyParser = require("body-parser");
const app = express();
const port = 8080;
app.use(bodyParser.json());
// Add headers before the routes are defined
app.use(function (req, res, next) {
	// Website you wish to allow to connect
	res.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");

	// Request methods you wish to allow
	res.setHeader(
		"Access-Control-Allow-Methods",
		"GET, POST, OPTIONS, PUT, PATCH, DELETE"
	);

	// Request headers you wish to allow
	res.setHeader(
		"Access-Control-Allow-Headers",
		"X-Requested-With,content-type"
	);

	// Set to true if you need the website to include cookies in the requests sent
	// to the API (e.g. in case you use sessions)
	res.setHeader("Access-Control-Allow-Credentials", true);
	res.setHeader("Access-Control-Allow-Headers", "*");

	// Pass to next layer of middleware
	next();
});

// app.use((req, res, next) => {
// 	setTimeout(() => next(), 2000);
// });

app.put("/api/trainees", (req, res) => {
	user = {
		...user,
		...req.body,
	};

	res.json(user);
});

app.put("/api/trainers", (req, res) => {
	trainerUser = {
		...trainerUser,
		...req.body,
	};

	res.json(trainerUser);
});

let trainerUser = {
	firstName: "Biba",
	lastName: "SomeLastName",
	username: "username123",
	dateOfBirth: "2023-11-11",
	address: "Somewhere, Nowhere, USA",
	email: "some@gmail.com",
	specialization: "zumba",
	active: true,
	trainees: [
		{
			username: "test",
			firstName: "Trainee1FN",
			lastName: "Trainee1LN",
			isActive: true,
		},
		{
			username: "test2",
			firstName: "Trainee2FN",
			lastName: "Trainee2LN",
			isActive: false,
		},
	],
};
let user = {
	firstName: "Biba",
	lastName: "SomeLastName",
	username: "username123",
	dateOfBirth: "2023-11-11",
	address: "Somewhere, Nowhere, USA",
	email: "some@gmail.com",
	active: true,
	trainers: [
		{
			username: "test",
			firstName: "Trainer1FN",
			lastName: "Trainer1LN",
			specialization: "Pilates",
		},
		{
			username: "test2",
			firstName: "Trainer12N",
			lastName: "Trainer2LN",
			specialization: "Yoga",
		},
	],
};
app.get("/api/trainees/profile", (req, res) => {
	res.json(user);
});

app.post("/api/trainers", (req, res) => {
	res.json({
		username: "someUserName",
		password: "3rgwkdf;wl",
	});
});

app.put("/api/auth/change-password", (req, res) => {
	res.status(204).send();
	//res.send("Password changed!");
});

app.post("/api/auth/login", (req, res) => {
	if (req.body.username === "trainer") {
		res.json({
			accessToken: "at",
			refreshToken: "rt",
			userType: "trainer",
		});

		return;
	}

	res.json({
		accessToken: "at",
		refreshToken: "rt",
		userType: "trainee",
	});
});
app.post("/api/auth/logout", (req, res) => {
	res.status(200).send();
});
app.post("/api/trainees", (req, res) => {
	res.json({
		username: "someUserName",
		password: "3rgwkdf;wl",
	});
});
app.post("/api/trainings/add", (req, res) => {
	res.json({});
});

app.delete("/api/trainees", (req, res) => {
	res.send("deleted!");
});

app.get("/api/trainers/profile", (req, res) => {
	res.json(trainerUser);
});

app.get("/api/trainers/info", (req, res) => {
	res.json([
		{
			username: "biba",
			firstName: "BiBa",
			lastName: "The Thrird",
			specialization: "yoga",
		},
		{
			username: "yukari",
			firstName: "Yukari",
			lastName: "Takeda",
			specialization: "yoga",
		},
		{
			username: "zumbaman",
			firstName: "Zumba",
			lastName: "Dude",
			specialization: "zumba",
		},
	]);
});

app.post("/api/trainings/trainer/list", (req, res) => {
	res.json([
		{
			trainingDate: "2021-01-01",
			trainingName: "Pilates",
			trainingType: "Yoga",
			traineeName: "Jack Trainee",
			trainingDuration: 2,
		},
		{
			trainingDate: "2022-02-02",
			trainingName: "Pilates",
			trainingType: "Yoga",
			traineeName: "Noob Trainee",
			trainingDuration: 2,
		},
		{
			trainingDate: "2023-03-03",
			trainingName: "Pilates",
			trainingType: "Yoga",
			traineeName: "Bodybuilder Trainee",
			trainingDuration: 2,
		},
	]);
});

app.post("/api/trainings/trainee/list", (req, res) => {
	res.json([
		{
			trainingDate: "2021-01-01",
			trainingName: "Pilates",
			trainingType: "Yoga",
			trainerName: "Biba Trainer",
			trainingDuration: 2,
		},
		{
			trainingDate: "2022-02-02",
			trainingName: "Pilates",
			trainingType: "Yoga",
			trainerName: "Biba Trainer",
			trainingDuration: 2,
		},
		{
			trainingDate: "2023-03-03",
			trainingName: "Pilates",
			trainingType: "Yoga",
			trainerName: "Biba Trainer",
			trainingDuration: 2,
		},
	]);
});

app.listen(port, () => {
	console.log(`Example app listening on port ${port}`);
});
