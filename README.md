# 🔮 About Me
My name is Joe, I am 44 years old, married, and have kids. I started college in October 2022 and am pursuing a **Bachelor’s degree in Computer Science** with a minor in **Applied Mathematics**.

I am on track to begin my **Master’s in Management with a concentration in Construction Management** in February. With 23 years of experience in this field, I’m excited to see how technology is increasingly overlapping with construction and other industries.  
I completed my **CompTIA ITF+** certification and am currently studying **Security+**.

---

## 🔮 Start Professional Self-Assessment 12/01/25

As I continue evolving from ironworker to innovator, I plan to assess my growth in the following areas:

- **TEST Technical Mastery:** Deepen my expertise in database optimization, scalable backend systems, and secure mobile development.
- **TEST User-Centered Design:** Refine UI/UX strategies to enhance clarity, accessibility, and responsiveness across platforms.
- **TEST Workflow Modernization:** Integrate construction technologies like BIM, Procore, and mobile apps to streamline field operations.
- **TEST Strategic Leadership:** Transition into tech-enabled management roles that bridge field experience with digital innovation.

_Last updated: December 2025_

---

# 🔮 CS-499 Capstone and Artifact
For this course, I selected an inventory application I created during my CS-360 course called **InventoryMate**.  
The following is my reflection at the end of the course. I will follow up with a new reflection/assessment upon completion of the capstone.

I had never done much tinkering with apps before. While I’ve always appreciated them, I hadn’t considered developing one myself. Over the last few weeks, I had the opportunity to do so, and it was a great experience. I found it fascinating to design navigation between screens and build the logic that allows a button click to instantly move to the next screen.

My app still has features that can be implemented, and I plan to continue tinkering with it as part of my learning process. While I don’t currently intend to launch the app, I may consider it in the future. If that happens, I’ll research proper guidelines and address questions as they arise. Overall, my experience with Project Three — and the project as a whole — has been valuable in designing, implementing, and launching an application. This experience will help me in future endeavors, and if I do launch the app, I’ll have proven experience to showcase.

---

# 🎥 Code Review Video
<a href="https://youtu.be/KQWZkkFlBgY" target="_blank"
   style="display:inline-block; padding:10px 20px; background-color:#007acc; color:#fff;
          text-decoration:none; border-radius:5px; font-weight:bold;">
   ▶ Watch My Code Review
</a>

---

# 🔮 Enhancement One: Software Design and Engineering

---

## 📌 Brief Description of the Artifact
**InventoryMate** is a Kotlin-based Android application designed to simplify inventory tracking for users managing supplies, tools, or stock.  
Created in **Fall 2025**, the app allows users to:
- Log in securely
- Add and update items
- Receive SMS alerts when inventory levels fall below a defined threshold

It features:
- Grid-based item display  
- Persistent storage via **SQLite**  
- Modern permission handling for SMS functionality  

---

## 🎯 Justification for Inclusion in My ePortfolio
I selected **InventoryMate** because it demonstrates my ability to design and implement a full-stack mobile solution that responds to real-world operational needs. It reflects my growth in software development, particularly in:

- Modular architecture and clean UI design  
- Database integration using SQLite  
- System service interaction via **SmsManager**  
- Session management with **SharedPreferences**  
- Modern Android practices like **ActivityResultContracts** and **KTX extensions**  

This artifact bridges my **23 years of field experience in construction and logistics** with my evolving technical skillset, showcasing how I can translate workflow challenges into scalable digital tools.

---

## 🛠️ Key Components That Showcase My Skills
- SMS alert system triggered by low-stock conditions  
- Permission handling that adapts to user responses  
- Dynamic message generation with item-specific details  
- Debugging and logging using **Logcat** to trace silent failures  
- Session-based personalization for user-specific notifications  

---

## ✅ Course Outcomes Met
I successfully met the Module One outcomes I planned for:
- Implementing mobile features that respond to real-world conditions  
- Enhancing user experience through automation and feedback  
- Applying Kotlin idioms and Android architecture principles  

---

## 🔍 Reflection on the Enhancement Process
Enhancing **InventoryMate** taught me how to:
- Debug silent failures by tracing execution with **Logcat**  
- Refactor hardcoded logic into dynamic, data-driven flows  
- Use **KTX extensions** for cleaner, idiomatic Kotlin  
- Ensure cross-activity data persistence with **SharedPreferences**  

**Challenges faced:**
- Diagnosing why SMS wasn’t triggering due to missing session data  
- Ensuring compatibility across Android versions for **SmsManager**  
- Balancing UI feedback with background logic  
- Handling permission denial gracefully while maintaining app functionality  

This process deepened my understanding of **Android lifecycle events**, **permission models**, and the importance of traceable logging in mobile development. It also reinforced the value of **modular design** and **user-centric feedback** in building reliable applications.

---

# 🔮 Enhancement Two: Algorithms and Data Structure

---

## 📌 Brief Description of the Artifact
The artifact for **Milestone Three** is the search and filter enhancement implemented in my Kotlin-based Android application, **InventoryMate**. Created in **Fall 2025**, this enhancement introduces efficient algorithms and data structures to improve inventory management.  

Key improvements include:
- Replacing linear search with **binary search** on sorted lists  
- Adding filtering capabilities based on **category** and **quantity**  
- Seamless integration with the **RecyclerView** display for fast item management  

---

## 🎯 Justification for Inclusion in My ePortfolio
I selected this artifact because it demonstrates my ability to apply **algorithmic thinking** and **data structure design** to a practical mobile application. It highlights my growth in computer science by showing how I can optimize performance and scalability through:

- Binary search implementation for efficient lookups  
- Modular utility functions for reusable search and filter logic  
- Structured data modeling with category support  
- Performance optimization in RecyclerView using **DiffUtil**  
- Integration of algorithms into a user-facing interface  

This artifact bridges **theoretical knowledge of algorithms** with **real-world application development**, strengthening my ePortfolio by showing that I can build functional apps and enhance them with efficient, maintainable, and scalable solutions.

---

## 🛠️ Key Components That Showcase My Skills
- Binary search replacing linear search for improved efficiency  
- Filter functions supporting category and quantity constraints  
- Modular design in `InventoryUtils.kt` for reusable algorithmic logic  
- DiffUtil integration for optimized RecyclerView updates  
- Database schema updates to support structured data with categories  

---

## ✅ Course Outcomes Met
I successfully met the Module One outcomes I planned for:
- Implementing and evaluating algorithms to improve application performance  
- Designing and applying data structures to support scalable solutions  
- Integrating algorithmic logic into user-facing features for enhanced usability  

---

## 🔍 Reflection on the Enhancement Process
Enhancing **InventoryMate** with search and filter functionality taught me how to:
- Apply binary search to real-world datasets for performance gains  
- Refactor data models to support structured filtering (adding category fields)  
- Use **DiffUtil** to optimize RecyclerView updates and reduce unnecessary redraws  
- Balance algorithmic improvements with user interface integration  

**Challenges faced:**
- Updating the database schema and queries to include category support  
- Debugging type mismatches when refactoring `InventoryItem` and adapter logic  
- Ensuring filters worked seamlessly when combined with search queries  
- Managing UI state across multiple filter inputs (search bar, spinner, seekbar)  

This process deepened my understanding of how **algorithms and data structures directly impact application performance and user experience**. 
It reinforced the importance of **modular design**, **efficient data handling**, and thoughtful integration of algorithmic logic into real-world software solutions.

---

# 🔮 Enhancement Three: Databases

---

## 📌 Brief description of the artifact
The artifact for **Milestone Four** is the enhanced version of my Kotlin-based Android application, **InventoryMate**, 
developed during my software development coursework in **2025**. 
This enhancement focuses on robust database design, defensive programming, and interoperability features to improve data integrity and user experience.

**Key improvements include:**
- Category assignment using a normalized SQLite schema with **foreign key constraints**
- **Case-insensitive** item handling to prevent duplicate entries
- **CSV export** functionality for clean external data use
- UI refinements with **spinners**, **RecyclerView adapters**, and **dynamic filtering**

---

## 🎯 Justification for inclusion in my ePortfolio
I selected this artifact because it demonstrates my ability to apply **software engineering best practices** to a real-world application. 
It showcases my growth in full-stack development and highlights how I iteratively improved InventoryMate to support scalability, reliability, and usability.

**Key skills demonstrated include:**
- **Database normalization** with category tables and foreign key constraints
- **Defensive programming** using null safety, error handling, and case-insensitive queries
- **UI development** with dynamic filtering and polished item presentation
- **CSV export** integration for external data sharing and auditability

These enhancements transformed InventoryMate from a basic inventory tracker into a robust, 
user-friendly tool with strong data integrity and professional polish.

---

## 🛠️ Key components that showcase my skills
- **Normalized SQLite schema:** Category management via foreign keys and clean relationships
- **Case-insensitive matching:** SQL `LOWER()` usage to prevent duplicates
- **Defensive programming:** Null safety, validation, and clear error handling
- **CSV export:** Formatted output for external use and auditing
- **UI refinements:** Capitalized display names and dynamic filter controls

---

## ✅ Course outcomes met
I successfully met the Module One outcomes I planned for:
- **Full-stack features** with database integration
- **Defensive programming** to improve reliability and maintainability
- **User experience enhancements** through thoughtful UI design and data handling

I also expanded my outcome coverage by integrating **CSV export** and 
refining **data normalization**, which became essential as the project evolved.

---

## 🔍 Reflection on the enhancement process
Enhancing **InventoryMate** with database and defensive programming features taught me how to:
- Normalize data to prevent inconsistencies and improve query reliability
- Use SQL functions like `LOWER()` for case-insensitive matching
- Design UI components that balance clarity, functionality, and user control
- Implement export features that support interoperability and external reporting

**Challenges faced:**
- Debugging database queries that failed with mixed-case input
- Ensuring category IDs were correctly linked to inventory items
- Managing RecyclerView updates efficiently using **DiffUtil**

Each challenge required research, testing, and refinement. This process strengthened my **problem-solving skills, 
adaptability, and confidence** in building maintainable, user-centered applications.

