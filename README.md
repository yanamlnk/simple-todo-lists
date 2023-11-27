# Simple to-dos. Place for your lists :black_heart:
Time to complete: 3 days.

## Day 1
**1. Creating approximate design**

<img width="322" alt="image" src="https://github.com/yanamlnk/simple-todo-lists/assets/90959658/15026865-43f6-49d2-9691-0174e92bed30">

**2. Research:**
- [How do you make websites with Java?](https://stackoverflow.com/questions/621228/how-do-you-make-websites-with-java)
- [Java Web Application Tutorial for Beginners](https://www.digitalocean.com/community/tutorials/java-web-application-tutorial-for-beginners)
- [Spring Boot JSP Hello World Example Tutorial (Video)](https://www.youtube.com/watch?v=uBjeeUfnhUM&ab_channel=JavaGuides)
- :bangbang:[Build an ENTIRE TODO Web Application with Java Spring Boot 3.0.0 in 62 min (Video)](https://www.youtube.com/watch?v=yqWtYKWWcpY&ab_channel=WazooWebBytes)

:bulb: decided to use Spring Boot, Thymeleaf and HTML for application instead of JSP as those technologies are more familiar to me.

## Day 2

### BASIC ELEMENTS:
Generated a template using https://start.spring.io/.

### —— Application dependencies —— 

- H2 database
- Spring Web
- Spring Data JPA
- Thymeleaf
- Validation
- Spring Devtools

### —— Configuration ——

Defaul port `8080`.

In `application.properties` 
- Configure H2 database 
- Configure H2 console
- Configure Thymeleaf cache
- Configure JPA

### —— Backend —— 

**1. Entities**
```
class ToDoList {
    Long id;
    List <Element> elements
    String listName
}
```
```
class ToDoElement {
    Long id
    ToDoList todoList;
    String name;
    Boolean isDone;
}
```
- `@Entity` - to specify that this class is an entity
- `@Table` - to specify table name (indifferent from class name)
- `@Id` to specify id of the entity
- `@GeneratedValue` to generate id automatically
- `@NoBlank` - validator 

### RELATIONSHIPS:
_**ONE** `List` can have **MANY** `Elements`._

In `ToDoList`:
```
@OneToMany(mappedBy = "toDoList", cascade = CascadeType.ALL, orphanRemoval = true)
private List<ToDoElement> toDoElements = new ArrayList<>();
```
- `mappedBy` - to indicate field name in `ToDoElement` class
- `CascadeTyde.All` - all actions done on `ToDoList` will be cascaded to children
- `orphanRemoval` - in case `ToDoList` will be deleted, all child elements will be deleted, too.

In `ToDoElement`:
```
@ManyToOne
@JoinColumn(name = "toDoList_Id")
private ToDoList toDoList;
```
- `name` of the foreign key in the table

**2. Repositories** 

What actions will I need?
| Action | Repository | Method |
| --- | --- | --- |
| Add list | ToDoListRepository | ToDoList save(ToDoList toDoList) |
| Delete list | ToDoListRepository | void delete(ToDoList entity) |
| Edit name of the list | ToDoListRepository | ToDoList save(ToDoList toDoList) |
| Find list | ToDoListRepository | Optional<ToDoList> findById(Long id) |
| Find all lists | ToDoListRepository | List<ToDoList> findAll() |
| Add element to the list | ToDoElementRepository | ToDoElement save(ToDoElement toDoElement) - (setting ToDoList beforehand) |
| Delete element from the list | ToDoElementRepository | void delete(ToDoElement toDoElement) |
| Mark element as done | ToDoElementRepository | ToDoElement save(ToDoElement toDoElement) - (setting boolean beforehand) |
| Edit name of the element  | ToDoElementRepository | ToDoElement save(ToDoElement toDoElement) - (setting new name beforehand) |
| Find element | ToDoElementRepository | Optional<ToDoElement> findById(Long id) |

**3. Controller**

Start from Home Page Controller with `@GetMapping(“/)` method
- Returns ModelAndView object - object for rendering views with associated data. 
- “home” argument in the constructor - name of the “View” (so name of the html file). 
- addObject method adds some data to the Model. “toDoLists” is keyword that will be used to refer to that data in HTML file, toDoListRepository.findAll() is the data itself)

<sub> :bulb:(P.S. MVC model - Model View Controller model. Model responsible for data, View for UI, Controller - for taking input from view and manipulating the Model based on the input. Then Model updates View with new data)	</sub> 

### —— Frontend ——

`home.html`

Responsible for home page
- In `head` configure encoding, colours, and viewport to be visible from different devices
- In `body` is a heading, and then `div` (element) of each list and its elements in a form of a table. Lists are extracted using `Thymeleaf`:
  - `<div th:each = "list : ${toDoLists}"></div>` - element where program is iterating through all the lists
  - `th:href="@{/create-element/{id}(id=${list.id})}` - create a reference using `id` field of `list` object
  - `<tr th:each="element : ${list.toDoElements}"></tr>` - show rows with help of elements' iteration, got from the `toDoElements` field of `list` object
  - `<span th:if="${element.isDone}"></span>` or `<span th:unless="${element.isDone}"></span>` for conditions
  - `th:text="${element.name}"` show `name` of the `element`
  - `th:object="${toDoList}"` or `th:field="${toDoList.name}"` object or field reference

### —— Test run ——

`Error: Table "LISTS" not found (this database is empty);` -> Don’t forget to put in `application.properties` the following:
```
spring.jpa.hibernate.ddl-auto=create-drop
```
*“The values create, create-drop, validate, and update basically influence how the schema tool management will manipulate the database schema at startup.”*

:bulb: Usually used for tests, but here it is better to use this property to create database and tables, and then drop it after program stops. Not recommended for production. 

### —— Additional Features ——

**1. Button to add the list**
- under the heading 
- when you click on it, it opens new page where you fill information on new list

```
________________________________________

|Back|__(name of the list)__|Create|
________________________________________
```

- Create button in `home.html` under the heading, with new endpoint
- Create new controller class, create `GetMapping` method for endpoint from the step 1. Returns new html page (`create-list-page`)
- In `create-list-page.html` add:
	- Back button -> returns to home page
	- Form to accept name of the new list AND create new endpoint to POST
	- Submit button
- In class from the step 2, create PostMapping to the endpoint from step 3, to save new element to the database

**2. Button to add new element to the list**
- at the end of each list
- when you click on it, it opens new page where you fill information on new element
__________
- Create button in `home.html` under the table, with new endpoint that contains list id
- In controller class, create GetMapping method for endpoint from the step 1. Returns new html page (`create-element-page`)
- In create-element-page.html add:
	- Back button -> returns to home page
	- Form to accept name of the new list AND create new endpoint to POST (again that contains list id)
	- Submit button
- In class from the step 2, create PostMapping to the endpoint from step 3, to save new element to the database

**3. Ability to complete the element**
- before name of the element
- button “Done!”
- When element is done, it has strikethrough effect
- when click again, the element is “undone”
_________
To do this, add new button in home.html that will have GET endpoint. In controller class, created method that will change isDone field to the opposite.

## Day 3
**4. Edit button to the list to edit name and delete list**
- to the left of the list
- 2 separate buttons, one for Delete, another for Edit. 
_________
- Delete -> as with complete button, simply got you to the endpoint where the element is deleted based on its id.
- Edit -> as in button to add new element. It get to the new page where you can rename and submit new name.

**5. Edit button to each of the elements to edit name or delete element**

The same steps and process as in previous stage

**6. Done button must change to “Undone” button if task is completed**

Added extra conditional wrapper around “Done” button. The functionality of the button is the same, the only difference is title of the button.
- changed text to “Not yet…”

### —— Make it pretty ——
The look right now after all the functionality is done:

Home page:

![image](https://github.com/yanamlnk/simple-todo-lists/assets/90959658/1082751c-eb60-43ab-927f-e86d87fea7b1)

Create new list/element:

![image](https://github.com/yanamlnk/simple-todo-lists/assets/90959658/1ab03c45-1afc-4e9b-a64d-99eb0ef8e191)

Rename list/element:

![image](https://github.com/yanamlnk/simple-todo-lists/assets/90959658/89f2873e-a8d0-4f4b-9738-d097f75af9bd)


**1. Adding icons instead of words for the buttons** 
- Using https://fontawesome.com/icons
- Add link inside `<head>` tag
```
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
```
:bulb: This will work only with internet connection, not very stable. To improve - must download css files and refer to them locally

Example of button using icon:
```
 <a href="/">
    <button type="button"><i class="fas fa-arrow-left fa-icon"></i></button>
 </a>
```

**2. CSS**
- Make correct positions of elements 
  - Move Back buttons in edit and create pages
  - Center all titles, formes, and +list button
  - Create space between lists
  - Make lists in one row, not in one column

- Make everything prettier
  - Change font
  - Make everything bigger
  - Make appropriate buttons (where needed - change colours, or make invisible background)

Done with different elements of CSS (added in HTML file in block `<head>` -> `<style>`)
- `body` - styles for the entire body of the HTML document, including background color, layout as a flex container with centered content
- `h1` and `h3` - styles for header elements with specific colors, margins, and font family
- `.list-container` - styles for a container div that wraps lists. It uses flex properties for layout
- `table` - styles for tables, including margins, borders, and border-radius
- `th, td` - styles for table header and data cells, including padding, text alignment, font size, and color
- `button` - styles for buttons, including padding, background color, text color, no borders, border-radius, cursor, and a transition effect on hover. Cursor set to pointer for interaction
- `.fa-icon`, `.fa-check-circle`, `.fa-times-circle` - styles for specific icons using Font Awesome, including colors
- `.add-list-button` - styles for a specific button with a solid background, specific colors, and padding
- `form` - styles for a form container with flex layout, spacing, background color, padding, border-radius, and box shadow
- `input` - styles for form input fields, including flex properties, padding, margins, borders, and border-radius

## Result:
Home:

![image](https://github.com/yanamlnk/simple-todo-lists/assets/90959658/e6895c94-eadf-4d6a-9e75-75ac6dc659b4)

New list/element:

![image](https://github.com/yanamlnk/simple-todo-lists/assets/90959658/8d3b0343-3ac5-41bb-8d35-a69591d6158f)

Edit list/element:

![image](https://github.com/yanamlnk/simple-todo-lists/assets/90959658/a95a7055-37ba-46c2-8406-776601b4d230)

<sub>Actual tables are smaller in real screen. Screenshots are taken ignoring the blank space</sub>

### —— Ways to improve ——
- Add users
- Add functionality to edit button of the list and element (for example, add deadlines, priority, tags)
- Create dynamic pages (add lists and elements on the same page)
- And much more :)

