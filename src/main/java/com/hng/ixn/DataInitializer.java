package com.hng.ixn;

import com.hng.ixn.content.Content;
import com.hng.ixn.content.ContentRepository;
import com.hng.ixn.user.Role;
import com.hng.ixn.user.User;
import com.hng.ixn.user.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ContentRepository contentRepository;

    @Value("${data.initialize:true}")  // Default to true if not specified
    private boolean initialize;

    @PostConstruct
    public void init() throws Exception {
        if (initialize) {
            initializeUsers();
            initializeContent();
        } else {
            System.out.println("Data initialization is skipped.");
        }
    }

    private void initializeUsers() {
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = User.builder()
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("SecurePassword123"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
        if (userRepository.findByEmail("user@example.com").isEmpty()) {
            User user = User.builder()
                    .email("user@example.com")
                    .password(passwordEncoder.encode("SecurePassword123"))
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
        if (userRepository.findByEmail("a").isEmpty()) {
            User admin = User.builder()
                    .email("a")
                    .password(passwordEncoder.encode("a"))
                    .role(Role.ROLE_ADMIN)
                    .build();
            userRepository.save(admin);
        }
        if (userRepository.findByEmail("u").isEmpty()) {
            User user = User.builder()
                    .email("u")
                    .password(passwordEncoder.encode("u"))
                    .role(Role.ROLE_USER)
                    .build();
            userRepository.save(user);
        }
        System.out.println("Users initialized");
    }

    private void initializeContent() {
        // Clear existing data
        contentRepository.deleteAll();

        // Insert new data
        contentRepository.saveAll(List.of(
                Content.builder()
                        .id(1)
                        .title("Ashford and St Peter's")
                        .description("Ashford and St Peter's Hospital Information.")
                        .content("""
                                <h2>Parking</h2>
                                    <p>Multi-story parking is available. Good availability before 9am. Cost is £20 monthly.</p>
                                                                
                                    <h2>Induction</h2>
                                    <p>Complete induction online at <a href="https://surreysafecaretraining.net/login/index.php">Surrey Safe Care</a> and <a href="https://asph.trainingtracker.co.uk/">ASPH Training Tracker</a>.</p>
                                                                
                                    <h2>Annual Leave</h2>
                                    <p>Email Rota Registrar for annual leave requests.</p>
                                                                
                                    <h2>Study Leave</h2>
                                    <p>Apply and claim study leave forms online at <a href="https://education.asph.nhs.uk/ecPG.php#">ASPH Study Leave</a>.</p>
                                                                
                                    <h2>Relocation Expenses</h2>
                                    <p>Email <a href="mailto:asp-tr.medicalworkforce@nhs.net">asp-tr.medicalworkforce@nhs.net</a> for relevant paperwork.</p>
                                                                
                                    <h2>Service Provision</h2>
                                    <p>
                                        <strong>ST1</strong><br>
                                        1 fluoroscopy list Monday AM - send to Dr. Benning.<br>
                                        <strong>ST2 and above</strong><br>
                                        1 hot seat phone answering 2797.<br>
                                        1 acute CT reporting. Use Code 5 for malignancies.<br>
                                        1 inpatient ultrasound - Check CRIS Outstanding Orders and inform RDA of scans needed.
                                    </p>
                                                                
                                    <h2>On-Calls</h2>
                                    <p>Evenings 5-8pm. Saturday 9-5pm, including inpatient acute USS.</p>
                                                                
                                    <h2>PACS and Logins</h2>
                                    <p>PACS Email: <a href="mailto:asph.pacs@nhs.net">asph.pacs@nhs.net</a>. Computer Login – Initial Lastname.</p>
                                                                
                                    <h2>Tips and Tricks</h2>
                                    <p>CTRL + M = MPR. CTRL + A/S/C = MPR Axial, Sagittal, Coronal. Use PACS to check image quality.</p>
                                                                
                                    <h2>Local Teaching</h2>
                                    <p>Wednesday lunchtimes in the Education Centre.</p>
                                                                
                                    <h2>Timetables</h2>
                                    <p><a href="#">Attachment</a></p>
                                                                
                                    <h2>Useful Contacts</h2>
                                    <p>
                                        <strong>Consultants:</strong><br>
                                        Dr. Benning – GI<br>
                                        Dr. Yates – Neuro<br>
                                        Dr. Turner – IR<br>
                                        Dr. Evans – Chest<br>
                                        <strong>Radiographers:</strong><br>
                                        Sarah Parker - USS <a href="mailto:sarah.parker@nhs.net">sarah.parker@nhs.net</a><br>
                                        Tom Richards - CT <a href="mailto:tom.richards@nhs.net">tom.richards@nhs.net</a><br>
                                        <strong>Secretaries:</strong> <a href="mailto:asph.radiology@nhs.net">asph.radiology@nhs.net</a>
                                    </p>
                                """)
                        .nextId(2)
                        .prevId(null)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .secret("<p>This secret is visible to users and admins, but not guests</p><p>Email: abc@email.com</p><p>Password: 123</p>")
                        .build(),
                Content.builder()
                        .id(2)
                        .title("East Surrey")
                        .description("East Surrey Hospital Information.")
                        .content("""
                                 <h2>Parking</h2>
                                    <p>On site – good availability, now ticket system. Rugby Club before the aqueduct.</p>
                                                                
                                    <h2>Induction</h2>
                                    <p>Surrey Safe Care</p>
                                                                
                                    <h2>Annual Leave</h2>
                                    <p>Email Dr. Sellars: <a href="mailto:Naomi.Sellars@nhs.net">Naomi.Sellars@nhs.net</a></p>
                                                                
                                    <h2>Study Leave</h2>
                                    <p>Apply 8 weeks in advance via attached form with fee estimates. If supervisors cannot sign, email <a href="mailto:sash.studyleave@nhs.net">sash.studyleave@nhs.net</a>. Claim at: <a href="https://sash.easy.giltbyte.com/user/login/">https://sash.easy.giltbyte.com/user/login/</a></p>
                                                                
                                    <h2>Relocation Expenses</h2>
                                    <p>Fill in eligibility form and email to <a href="mailto:louise.wilson6@nhs.net">louise.wilson6@nhs.net</a>. Claim at <a href="https://sash.easy.giltbyte.com/user/login/">https://sash.easy.giltbyte.com/user/login/</a></p>
                                                                
                                    <h2>Service Provision</h2>
                                    <p>ST2 and above:</p>
                                    <ul>
                                        <li>1 fluoroscopy list (barium swallows) – Thursday AM, Tuesday PM. Usually 3 patients per list, vetted by Jordan/ Dr. Najdafi.</li>
                                        <li>1 inpatient ultrasound list (usually Friday afternoon). Vet via Appointment system (Cerner), ask RDA to call patients.</li>
                                        <li>1 duty rad/acute CT. Vet via PACS system – Urgency system 1-5 (Non-urgent to moribund). Z5 – if suspicion of cancer. Report and DRAFT (Clinician will be able to see draft).</li>
                                    </ul>
                                                                
                                    <h2>On-Calls</h2>
                                    <p>Sparse: Evening 5-8pm, Saturday 9-2pm</p>
                                                                
                                    <h2>PACS and Logins</h2>
                                    <p>Pacs Email: <a href="mailto:sash.pacsteam@nhs.net">sash.pacsteam@nhs.net</a></p>
                                    <p>Computer Login - Lastname.Firstname</p>
                                    <p>Dragon voice recognition</p>
                                    <p>WiFi: MAC Computer – KSSradiology</p>
                                                                
                                    <h2>Timetables</h2>
                                    <p>Attachment</p>
                                                                
                                    <h2>Useful Contacts</h2>
                                    <p>College Tutor: Naomi Sellars - <a href="mailto:Naomi.Sellars@nhs.net">Naomi.Sellars@nhs.net</a></p>
                                    <p>Consultants:</p>
                                    <ul>
                                        <li>Samantha Negus - paediatrics</li>
                                        <li>Jeremy Vive - paediatrics</li>
                                        <li>Stephen Gwyther</li>
                                        <li>Ana Petanac - GI</li>
                                        <li>Ehab Ismail - GU</li>
                                        <li>Alberto Villanueva - Chest</li>
                                        <li>Tony Newman-Sanders - MSK</li>
                                        <li>Ivan Walton - IR</li>
                                        <li>Ajay Pankania - IR</li>
                                        <li>Dr. Nick - IR</li>
                                    </ul>
                                    <p>Reporting Radiographers:</p>
                                    <ul>
                                        <li>Paul Matthews – Chest and MSK - <a href="mailto:paul.matthews@nhs.net">paul.matthews@nhs.net</a></li>
                                        <li>Karen Rosamond – Chest and Abdomen - <a href="mailto:karen.rosamond@nhs.net">karen.rosamond@nhs.net</a></li>
                                        <li>Grace Dark – MSK - <a href="mailto:Grace.Dark@nhs.net">Grace.Dark@nhs.net</a></li>
                                        <li>Clare Downs – Chest - <a href="mailto:clare.downs@nhs.net">clare.downs@nhs.net</a></li>
                                        <li>Connor Harper – MSK</li>
                                    </ul>
                                                                
                                    <h2>Local Teaching</h2>
                                    <p>Tuesday 12.15-13.00 – Dr. Vive</p>
                                    <p>Wednesday 8.00-9.00 – Dr. Sellars</p>
                                    <p>Friday 8.00 – 9.00 – Dr. Newman Sanders</p>
                                                                
                                    <h2>Audit Registration</h2>
                                    <p>To register, follow the instructions provided in attachments.</p>
                                """)
                        .nextId(3)
                        .prevId(1)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .secret("<p>Here's another secret...</p>")
                        .build(),
                Content.builder()
                        .id(3)
                        .title("Frimley")
                        .description("Frimley Hospital Information.")
                        .content("""
                                <h2>Parking</h2>
                                    <p>Fill in Parking and ID Form – difficult to park before 9am.</p>
                                                                
                                    <h2>Induction</h2>
                                    <p>Epic Modules</p>
                                                                
                                    <h2>Annual Leave</h2>
                                    <p>Email Dr. Savananthan: <a href="mailto:tharsi.sarvananthan@nhs.net">tharsi.sarvananthan@nhs.net</a></p>
                                                                
                                    <h2>Study Leave</h2>
                                    <p>Apply at: <a href="https://learning.fhft.nhs.uk/#/login">https://learning.fhft.nhs.uk/#/login</a>. Policies and Digital Forms (including Study Leave), then Digital Forms. Email <a href="mailto:Fhft.study.leave@nhs.net">Fhft.study.leave@nhs.net</a> for assistance.</p>
                                    <p>Claim at: <a href="https://sel-expenses.com/shared/logon.aspx?ReturnUrl=%2f">https://sel-expenses.com/shared/logon.aspx?ReturnUrl=%2f</a>. Company ID is fhft, username is staff number. Add vehicle for mileage.</p>
                                                                
                                    <h2>Relocation Expenses</h2>
                                    <p>Complete HEE Excess Mileage Form and email to <a href="mailto:fhft.erostering@nhs.net">fhft.erostering@nhs.net</a>. Claim at: <a href="https://sel-expenses.com/shared/logon.aspx?ReturnUrl=%2f">https://sel-expenses.com/shared/logon.aspx?ReturnUrl=%2f</a>. Company ID is fhft, username is staff number. Add vehicle for mileage (MOT + Tax).</p>
                                                                
                                    <h2>Service Provision</h2>
                                    <p>Usually expected x2 HOT SEAT sessions and x2 IP Ultrasound.</p>
                                                                
                                    <h2>On-Calls</h2>
                                    <p>Evenings 5pm – 10pm. Weekends 9am to 10pm, off Fridays and Mondays.</p>
                                                                
                                    <h2>PACS and Logins</h2>
                                    <p>Pacs Email: <a href="mailto:fhft.pacsteamfph@nhs.net">fhft.pacsteamfph@nhs.net</a></p>
                                    <p>Computer Login - InitialLastname</p>
                                                                
                                    <h2>Tips and Tricks</h2>
                                    <p>Refer to internal resources for additional tips.</p>
                                                                
                                    <h2>Timetables</h2>
                                    <p>Attachment</p>
                                                                
                                    <h2>Useful Contacts</h2>
                                    <p>College Tutor: Tharsi Sarvananthan - <a href="mailto:tharsi.sarvananthan1@nhs.net">tharsi.sarvananthan1@nhs.net</a></p>
                                    <p>Consultants:</p>
                                    <ul>
                                        <li>Dr. Pippa Skippage - Gynae</li>
                                        <li>Dr. Robert Barker - ALL but Neuro</li>
                                        <li>Dr. Kirstin Stafford - Breast / Paeds</li>
                                        <li>Dr. Tharsi Savananthan - Breast</li>
                                        <li>Dr. Lauren Ramsey - Paeds</li>
                                        <li>Dr. Tom Amies - MSK</li>
                                        <li>Dr. Brendan Barber - MSK</li>
                                        <li>Dr. Stephen Perrio - Gynae/H&N</li>
                                        <li>Dr. Emily Daulton - Nuclear Medicine</li>
                                        <li>Dr. David Clark - GU</li>
                                    </ul>
                                                                
                                    <h2>Local Teaching</h2>
                                    <p>Monday 12.30pm – Prof. Chris Clarke</p>
                                    <p>Friday 12.30pm – Dr. Robert Barker</p>
                                    <p>Tuesday 12.30pm – Dr. Tharsi Savananthan</p>
                                                                
                                    <h2>Audit Registration</h2>
                                    <p>To register, follow the instructions provided in attachments.</p>
                                """)
                        .nextId(4)
                        .prevId(2)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(4)
                        .title("Royal Surrey")
                        .description("Royal Surrey (Guildford) Hospital Information.")
                        .content("""
                                <h2>Parking</h2>
                                    <p>Multistory car park – APNR system, £40 monthly. Not open on weekends. Evening Code: 4175. Limited availability on the visitor car park.</p>
                                                                
                                    <h2>Induction</h2>
                                    <p>Complete online induction at <a href="https://learning.royalsurrey.nhs.uk/login/index.php">Royal Surrey Learning</a>. Email completed certificates to <a href="mailto:rsch.mylearningsupport@nhs.net">rsch.mylearningsupport@nhs.net</a>. Also, complete Surrey Safe Care induction at <a href="https://surreysafecaretraining.net/login/index.php">Surrey Safe Care</a>.</p>
                                                                
                                    <h2>Annual Leave</h2>
                                    <p>Email <a href="mailto:mariaarmstrong@nhs.net">Maria Armstrong</a>, copy in <a href="mailto:sanjin.idriz@nhs.net">Dr. Sanjin Idris</a>.</p>
                                                                
                                    <h2>Study Leave</h2>
                                    <p>Fill in the Study Leave Form with two signatures (Marie Nyoni and College Tutor). Ensure induction is complete before submitting study leave. Reimbursement requires printing attendance/receipt and filling in the form with the assignment number.</p>
                                                                
                                    <h2>Relocation Expenses</h2>
                                    <p>Complete the Relocation Eligibility Form and email to <a href="mailto:rsc-tr.MedicalResourcing@nhs.net">rsc-tr.MedicalResourcing@nhs.net</a>. Claim at <a href="https://royalsurrey.easy.gilbyte.com/user/login/">Royal Surrey Claims</a>.</p>
                                                                
                                    <h2>Service Provision</h2>
                                    <p>
                                        <strong>1 Acute CT Reporting</strong><br>
                                        Hot seat number 6404. Use CAT5 if concerns. Draft reports with Consultant and Author, set FOR REVIEW.<br>
                                        Bleep 71, Switch 9.<br>
                                        <strong>1 Inpatient USS</strong><br>
                                        Vet via Vetting List on CRIS, filter INPATIENTS, modality US. Inform RDA of scans needed.<br>
                                        <strong>1-2 Fluoroscopy</strong><br>
                                        Vet via Outstanding Orders on CRIS. Outpatient list every morning except Tuesday. Inpatient list Tuesday afternoon – write accepted ones in Fluoroscopy book. Send for review to the named consultant.<br>
                                        <strong>1 Shared Drains List</strong><br>
                                        Roughly 4 patients per list. Write accepted ones in Drains Book.
                                    </p>
                                                                
                                    <h2>On-Calls</h2>
                                    <p>Evenings 5-8pm, roughly 1 in 6. Saturday 9am – 8pm. Consultant will report from home; be prepared for loneliness. MRI queries can be directed to consultants.</p>
                                                                
                                    <h2>PACS and Logins</h2>
                                    <p>PACS Email: <a href="mailto:Rsc-tr.PACSTeam@nhs.net">Rsc-tr.PACSTeam@nhs.net</a>. Computer Login – InitialLastname.</p>
                                                                
                                    <h2>Tips and Tricks</h2>
                                    <p>CRTL + M = MPR. CRTL + A/S/C = MPR Axial, Sagittal, Coronal. Alt to align image. - to MIP.</p>
                                                                
                                    <h2>Local Teaching</h2>
                                    <p>Variable, Tuesday/Wednesday lunchtime if available.</p>
                                                                
                                    <h2>Timetables</h2>
                                    <p><a href="#">Attachment</a></p>
                                                                
                                    <h2>Useful Contacts</h2>
                                    <p>
                                        <strong>College Tutor:</strong> Dr. Saqib Butt - IR<br>
                                        <strong>Consultants:</strong><br>
                                        Dr. Jordan Green - Paediatrics<br>
                                        Dr. James Crawshaw - Everything<br>
                                        Dr. Fuad Hussain - IR/Gynae<br>
                                        Dr. Mark Ingram - Chest<br>
                                        Dr. Natasha Hamilton - Chest<br>
                                        Dr. James Russell - Breast<br>
                                        Dr. Aneet Sian - Breast<br>
                                        Dr. Price - Breast<br>
                                        Dr. Amit Shah - MSK<br>
                                        Dr. Matthew Mariathas - MSK<br>
                                        Dr. Chintu Gademsetty - MSK<br>
                                        Dr. Tom Puttick - MSK<br>
                                        Dr. Shelley Chapman - IR<br>
                                        Dr. Alex Horton - IR<br>
                                        Dr. Emma Wood - MSK/Everything<br>
                                        Dr. Kate Potter - Neuro<br>
                                        Dr. Sarah Watson - Neuro<br>
                                        Dr. Lilia Khafizova - Gynae<br>
                                        Dr. Sanjin Idriz - Head & Neck<br>
                                        <strong>Reporting Radiographers:</strong><br>
                                        Sophie Morgan - MSK <a href="mailto:sophie.morgan3@nhs.net">sophie.morgan3@nhs.net</a><br>
                                        Carole Stevens - MSK <a href="mailto:carol.stevens3@nhs.net">carol.stevens3@nhs.net</a><br>
                                        Laura Taylor - MSK<br>
                                        Chris Goodwin - MSK<br>
                                        Stephen Cheek - Chest <a href="mailto:stephen.cheek2@nhs.net">stephen.cheek2@nhs.net</a><br>
                                        Mike Jones - Chest <a href="mailto:mjones22@nhs.net">mjones22@nhs.net</a><br>
                                        Joe Sampson - Chest <a href="mailto:joesampson@nhs.net">joesampson@nhs.net</a><br>
                                        Mary Phillips - Chest/Abdomen <a href="mailto:mary.phillips1@nhs.net">mary.phillips1@nhs.net</a><br>
                                        <strong>Secretaries:</strong> <a href="mailto:rsch.radiologysecretaries@nhs.net">rsch.radiologysecretaries@nhs.net</a>
                                    </p>
                                                                
                                    <h2>Codes</h2>
                                    <p>Entrance: 41<br>Female Bathroom: 514<br>Staff Room: 234<br>CT/IR: 514</p>
                                                                
                                    <h2>Audit Registration</h2>
                                    <p>Complete registration at <a href="#">Attachment</a>. Email <a href="mailto:rsch.clinicalauditteam@nhs.net">rsch.clinicalauditteam@nhs.net</a>.</p>
                                """)
                        .nextId(5)
                        .prevId(3)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(5)
                        .title("Wexham")
                        .description("Wexham Hospital Information.")
                        .content("""
                                <h2>Parking</h2>
                                    <p>Parking is available for staff and visitors with a permit. Cost is £25 monthly. Limited availability on weekends.</p>
                                                                
                                    <h2>Induction</h2>
                                    <p>Complete induction via <a href="https://wexhampark.nhs.uk/induction">Wexham Park Induction</a> portal and send confirmation to <a href="mailto:wph.induction@nhs.net">wph.induction@nhs.net</a>.</p>
                                                                
                                    <h2>Annual Leave</h2>
                                    <p>Email <a href="mailto:hr.wexham@nhs.net">hr.wexham@nhs.net</a> for annual leave requests.</p>
                                                                
                                    <h2>Study Leave</h2>
                                    <p>Submit study leave requests through the <a href="https://wexhampark.nhs.uk/studyleave">Wexham Study Leave</a> portal.</p>
                                                                
                                    <h2>Relocation Expenses</h2>
                                    <p>Email <a href="mailto:wph.relocations@nhs.net">wph.relocations@nhs.net</a> for relocation expense queries.</p>
                                                                
                                    <h2>Service Provision</h2>
                                    <p>
                                        <strong>ST1</strong><br>
                                        1 acute CT reporting, phone 2698 for emergencies.<br>
                                        <strong>ST2 and above</strong><br>
                                        1 fluoroscopy list Wednesday PM.<br>
                                        1 inpatient USS - Check CRIS for pending requests.<br>
                                        1-2 shared drains lists per week.
                                    </p>
                                                                
                                    <h2>On-Calls</h2>
                                    <p>Evenings 5-8pm, one in 6 weekends. On-call duties include reporting and patient management.</p>
                                                                
                                    <h2>PACS and Logins</h2>
                                    <p>PACS Email: <a href="mailto:wph.pacs@nhs.net">wph.pacs@nhs.net</a>. Computer Login – Initial Lastname.</p>
                                                                
                                    <h2>Tips and Tricks</h2>
                                    <p>Use CTRL + M for MPR. Check image quality with PACS. For urgent queries, use internal bleep system.</p>
                                                                
                                    <h2>Local Teaching</h2>
                                    <p>Teaching sessions every Tuesday at 1pm in the Education Centre.</p>
                                                                
                                    <h2>Timetables</h2>
                                    <p><a href="#">Attachment</a></p>
                                                                
                                    <h2>Useful Contacts</h2>
                                    <p>
                                        <strong>Consultants:</strong><br>
                                        Dr. Ahmed – GI<br>
                                        Dr. Patel – Neuro<br>
                                        Dr. Williams – IR<br>
                                        Dr. Foster – Chest<br>
                                        <strong>Radiographers:</strong><br>
                                        Emma Johnson - USS <a href="mailto:emma.johnson@nhs.net">emma.johnson@nhs.net</a><br>
                                        Michael Lee - CT <a href="mailto:michael.lee@nhs.net">michael.lee@nhs.net</a><br>
                                        <strong>Secretaries:</strong> <a href="mailto:wph.radiology@nhs.net">wph.radiology@nhs.net</a>
                                    </p>
                                """)
                        .nextId(6)
                        .prevId(4)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(6)
                        .title("Academy")
                        .description("Educational programs and training.")
                        .content("""
                                <p><a href="https://nhs.sharepoint.com/sites/msteams_2536fb/Shared%20Documents/Forms/AllItems.aspx?FolderCTID=0x0120007644D8910E5E4948802E8E41E4981811&id=%2Fsites%2Fmsteams%5F2536fb%2FShared%20Documents%2FST2%20and%20ST3%20Teaching%2FRecordings&sortField=Modified&isAscending=true&viewid=d6c2f357%2D4dd2%2D492b%2Daf54%2Dea55f7b78ac1">Teaching Recordings</a></p>
                                <h3>Timetables</h3>
                                <ul>
                                    <li>ST1 – Wednesdays all day until FRCR Part 1, then half day</li>
                                    <li>ST2/3 – Thursdays PM</li>
                                    <li>ST4/5 – Tuesdays PM</li>
                                </ul>
                                """)
                        .nextId(7)
                        .prevId(5)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(7)
                        .title("ARCP")
                        .description("Annual Review of Competence Progression guidelines.")
                        .content("""
                                <h3>The Initial ST1 Checklist</h3>
                                <ul>
                                    <li>Sign up to Kaizen - risr/advance!</li>
                                    <li>Assign your Educational and Clinical Supervisor</li>
                                    <li>Join the RCR - <a href="https://portal.rcr.ac.uk/registration">https://portal.rcr.ac.uk/registration</a></li>
                                    <li>Form R (Part A) - <a href="https://trainee.tis.nhs.uk/">https://trainee.tis.nhs.uk/</a></li>
                                </ul>
                                <h3>Annual Forms</h3>
                                <ul>
                                    <li>Annual Form R (Part B) - <a href="https://trainee.tis.nhs.uk/">https://trainee.tis.nhs.uk/</a></li>
                                    <li>Fill in and Upload to Kaizen as a PDF</li>
                                    <li>12 DOPs, 12 Mini-IPX, 2 Teaching Session, 1 Audit</li>
                                    <li>Educational Supervisor Beginning, Mid and End of Year</li>
                                    <li>6 Clinical supervisor reports</li>
                                    <li>ES Structured Report</li>
                                    <li>MSF – 12 - Educational Supervisor to release</li>
                                    <li>Logbook: Upload evidence plain films
                                    <table border="1">
                                        <tr>
                                            <th></th>
                                            <th colspan="2">4 months</th>
                                            <th colspan="2">Annually</th>
                                        </tr>
                                        <tr>
                                            <th></th>
                                            <th>60%</th>
                                            <th>80%</th>
                                            <th>60%</th>
                                            <th>80%</th>
                                        </tr>
                                        <tr>
                                            <td>ST1</td>
                                            <td>325</td>
                                            <td>195</td>
                                            <td>260</td>
                                            <td>975</td>
                                            <td>585</td>
                                            <td>780</td>
                                        </tr>
                                        <tr>
                                            <td>ST2</td>
                                            <td>650</td>
                                            <td>390</td>
                                            <td>520</td>
                                            <td>1950</td>
                                            <td>1170</td>
                                            <td>1560</td>
                                        </tr>
                                        <tr>
                                            <td>ST3</td>
                                            <td>975</td>
                                            <td>585</td>
                                            <td>780</td>
                                            <td>2925</td>
                                            <td>1755</td>
                                            <td>2340</td>
                                        </tr>
                                        <tr>
                                            <td>ST4+</td>
                                            <td>1000</td>
                                            <td>600</td>
                                            <td>800</td>
                                            <td>3000</td>
                                            <td>1800</td>
                                            <td>2400</td>
                                        </tr>
                                    </table>
                                    </li>
                                </ul>
                                """)
                        .nextId(8)
                        .prevId(6)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(8)
                        .title("Conferences")
                        .description("Conferences and events for medical professionals.")
                        .content("""
                                <p>Full guidance: <a href="https://lasepgmdesupport.hee.nhs.uk/support/solutions/7000016490">https://lasepgmdesupport.hee.nhs.uk/support/solutions/7000016490</a></p>
                                                                
                                <h3>GI</h3>
                                <ul>
                                    <li>bsgar</li>
                                    <li>ESGAR</li>
                                </ul>
                                                                
                                <h3>Breast</h3>
                                <ul>
                                    <li>British Society of Breast Radiology</li>
                                    <li><a href="https://www.eusobi.org/">https://www.eusobi.org/</a></li>
                                </ul>
                                                                
                                <h3>Chest</h3>
                                <ul>
                                    <li>British Society of Thoracic Imaging</li>
                                    <li>European Society of Thoracic Imaging</li>
                                </ul>
                                                                
                                <h3>IR</h3>
                                <ul>
                                    <li>British Society of Interventional Radiology</li>
                                    <li>CIRSE</li>
                                    <li><a href="https://www.medall.com/events/rise24-radiological-imaging-and-intervention-symposium/">RiiSE24 - Radiological Imaging and Intervention Symposium</a></li>
                                </ul>
                                                                
                                <h3>Pediatrics</h3>
                                <ul>
                                    <li>British Society of Paediatric Radiology</li>
                                    <li>ESPR</li>
                                </ul>
                                                                
                                <h3>Emergency Radiology</h3>
                                <ul>
                                    <li>British Society of Emergency Radiology</li>
                                    <li>European Society of Emergency Radiology</li>
                                </ul>
                                                                
                                <h3>Urogenital</h3>
                                <ul>
                                    <li>British Society of Urogenital Radiology</li>
                                    <li>European Society of Urogenital Radiology</li>
                                </ul>
                                                                
                                <h3>Nuclear Medicine</h3>
                                <ul>
                                    <li>British Nuclear Medicine Society</li>
                                    <li>EANM</li>
                                </ul>
                                                                
                                <h3>Head & Neck</h3>
                                <ul>
                                    <li>BSHNI</li>
                                    <li>ESHNR.eu</li>
                                </ul>
                                                                
                                <h3>Neuroradiology</h3>
                                <ul>
                                    <li>British Society Of Neuroradiologists</li>
                                    <li>ESNR</li>
                                    <li>American Society of Pediatric Neuroradiology</li>
                                </ul>
                                                                
                                <h3>MSK</h3>
                                <ul>
                                    <li>British Society of Skeletal Radiologists</li>
                                    <li>European Society of Musculoskeletal Radiology</li>
                                </ul>
                                                                
                                <h3>USS</h3>
                                <ul>
                                    <li>BMUS</li>
                                    <li>EFSUMB</li>
                                </ul>
                                                                
                                <h3>National</h3>
                                <ul>
                                    <li>The Society of Radiologists in Training</li>
                                    <li>British Institute of Radiology</li>
                                </ul>
                                                                
                                <h3>National - Oncology</h3>
                                <ul>
                                    <li>Plans for UKIO 2024</li>
                                </ul>
                                                                
                                <h3>National - Surgery</h3>
                                <ul>
                                    <li>ASiT Annual Conference 2024</li>
                                </ul>
                                                                
                                <h3>European</h3>
                                <ul>
                                    <li>European Society of Radiology</li>
                                </ul>
                                                                
                                <h3>International</h3>
                                <ul>
                                    <li>RSNA</li>
                                </ul>
                                """)
                        .nextId(9)
                        .prevId(7)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(9)
                        .title("Educational Resources")
                        .description("Learning materials and resources for medical education.")
                        .content("""
                                <p>Radiopaedia</p>
                                <h3>e-IAMOS Anatomy</h3>
                                <ul>
                                    <li>Usernames: surreyspr1 - surreyspr5 @gmail.com</li>
                                    <li>Password: Sprsurrey1 - Sprsurrey5</li>
                                </ul>
                                <h3>STAD Dx</h3>
                                <p>SASH@KSS.com | staddx@KSS1</p>
                                <h3>MRI Online</h3>
                                <p>Contact Senan</p>
                                <h3>Society Recordings</h3>
                                <ul>
                                    <li>Webinar Recordings - BSNR</li>
                                    <li>Academy Virtual Learning 2023-24. Recordings. | BSGAR</li>
                                    <li>Username: AcEducation@123, Password: Access2AcEd!</li>
                                    <li>Radiology London</li>
                                    <li>Radiology Learning London Password: Panlondon2023</li>
                                </ul>
                                """)
                        .nextId(10)
                        .prevId(8)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build(),
                Content.builder()
                        .id(10)
                        .title("Exams")
                        .description("Examination schedules and preparation tips.")
                        .content("""
                                <h3>Booking system</h3>
                                <p><a href="https://examhub.rcr.ac.uk/candidate/login">https://examhub.rcr.ac.uk/candidate/login</a></p>
                                <h3>Resources</h3>
                                <ul>
                                    <li>Textbooks: <a href="https://drive.google.com/drive/folders/1-8UJDfCJeDhyT6cWfUjUCEMPbdfxz34x?usp=sharing">Textbooks</a></li>
                                    <li>Physics Resource: <a href="http://www.impactscan.org/">Impactscan</a></li>
                                </ul>
                                <h3>Revision Courses</h3>
                                <ul>
                                    <li>FRCR Part 1</li>
                                    <li>FRCR 2A</li>
                                    <li>FRCR 2B</li>
                                    <li>Anatomy</li>
                                    <li>Physics</li>
                                    <li>The Course – FRCR Review</li>
                                    <li>The FRCR 2b Rapid Reporting Courses</li>
                                    <li>Peninsula - FRCR 1 – 19th December 2022, 9th & 10th January 2023</li>
                                    <li>Edinburgh FRCR 2B Course</li>
                                    <li>Leicester - Leicester FRCR Part 1 revision courses</li>
                                    <li>Leicester Radiology Courses: East Midlands FRCR 2b course</li>
                                    <li>BIR Physics The 3rd ARI-BIR Joint FRCR Part 1 Physics Preparation Course 2022 | British Institute of Radiology</li>
                                    <li>Aunt Minnie Courses</li>
                                    <li>Mersey - FRCR Part 1 Mersey Anatomy Course</li>
                                    <li>Mersey – FRCR Part 1 Mersey Physics Course</li>
                                    <li>Virtual Tutes</li>
                                    <li>Stoke Radiology</li>
                                    <li>South West FRCR</li>
                                </ul>
                                """)
                        .nextId(null)
                        .prevId(9)
                        .timestamp(LocalDateTime.parse("2024-07-10T02:37:04"))
                        .build()
        ));
        System.out.println("Content data initialized");
    }
}
