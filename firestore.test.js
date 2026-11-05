const {
  initializeTestEnvironment,
  assertFails,
  assertSucceeds,
} = require("@firebase/rules-unit-testing");
const { test, before, after, beforeEach } = require("node:test");
const fs = require("node:fs");

let testEnv;
const PROJECT_ID = process.env.GCP_PROJECT || "demo-no-project";
const ALICE_UID = "alice_123";
const BOB_UID = "bob_456";

const [emulatorHost, emulatorPortStr] = (process.env.FIRESTORE_EMULATOR_HOST || "127.0.0.1:8085").split(":");
const emulatorPort = parseInt(emulatorPortStr, 10);

before(async () => {
  const rules = fs.readFileSync("./firestore.rules", "utf8");
  testEnv = await initializeTestEnvironment({
    projectId: PROJECT_ID,
    firestore: {
      rules,
      host: emulatorHost,
      port: emulatorPort,
    },
  });
});

after(async () => {
  if (testEnv) {
    await testEnv.cleanup();
  }
});

beforeEach(async () => {
  if (testEnv) {
    await testEnv.clearFirestore();
  }
});

test("Unauthenticated user: cannot read users", async () => {
  const unauthDb = testEnv.unauthenticatedContext().firestore();
  await assertFails(unauthDb.collection("users").doc(ALICE_UID).get());
});

test("Authenticated user: can create their own profile", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertSucceeds(
    aliceDb.collection("users").doc(ALICE_UID).set({
      userId: ALICE_UID,
      email: "alice@example.com",
      displayName: "Alice Reporter",
      photoUrl: null,
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});

test("Authenticated user: cannot read another user's profile", async () => {
  await testEnv.withSecurityRulesDisabled(async (context) => {
    await context.firestore().collection("users").doc(BOB_UID).set({
      userId: BOB_UID,
      email: "bob@example.com",
      displayName: "Bob Reporter",
      photoUrl: null,
      createdAt: new Date(),
      updatedAt: new Date(),
    });
  });

  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  await assertFails(aliceDb.collection("users").doc(BOB_UID).get());
});

test("Authenticated user: can create and read their own campaign report", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const reportRef = aliceDb.collection("users").doc(ALICE_UID).collection("reports").doc("report_001");

  await assertSucceeds(
    reportRef.set({
      userId: ALICE_UID,
      theme: "GROUND_REALITY",
      village: "Chhamrachh",
      tehsil: "Sarai",
      district: "Singrauli",
      state: "Madhya Pradesh",
      claim: "100% tap water connection under Jal Jeevan Mission",
      groundReality: "Taps dry, villagers waiting in line for handpump",
      tone: "INVESTIGATIVE",
      visualProofSummary: "Handpump with long queue of empty pots",
      suggestedHashtags: ["#JalJeevanMission", "#GroundReality"],
      alternativeTones: ["Constructive Critique"],
      twitterHindi: "छमरछ में जल जीवन मिशन का दावा vs हकीकत",
      twitterEnglish: "Ground reality of tap water in Chhamrachh",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );

  await assertSucceeds(reportRef.get());
});

test("Authenticated user: cannot write report for another user", async () => {
  const aliceDb = testEnv.authenticatedContext(ALICE_UID).firestore();
  const bobReportRef = aliceDb.collection("users").doc(BOB_UID).collection("reports").doc("report_002");

  await assertFails(
    bobReportRef.set({
      userId: ALICE_UID,
      theme: "GROUND_REALITY",
      village: "Chhamrachh",
      district: "Singrauli",
      claim: "Tap water claim",
      groundReality: "No water",
      createdAt: new Date(),
      updatedAt: new Date(),
    })
  );
});
