package com.project.ridealong.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.ridealong.dao.RidersDao;
import com.project.ridealong.entities.Riders;

import jakarta.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class MyController {

	@Autowired
	RidersDao ridersDao;
	
	 @GetMapping("/")
	    public void index(HttpServletResponse response) throws IOException {
	        ClassPathResource resource = new ClassPathResource("static/index.html");
	        response.getOutputStream().write(resource.getInputStream().readAllBytes());
	    }
	
	
	
	
	@GetMapping("/api/riders")
	public ResponseEntity<List<Riders>> getAllRiders(@RequestParam(required = false) String email) {
		 try {
		      List<Riders> riders = new ArrayList<Riders>();

		      if (email == null)
		        ridersDao.findAll().forEach(riders::add);
		      else
		        ridersDao.findByEmailContaining(email).forEach(riders::add);

		      if (riders.isEmpty()) {
		        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		      }

		      return new ResponseEntity<>(riders, HttpStatus.OK);
		    } catch (Exception e) {
		      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		    }
		  }
	
	
	
	
	
	

	  @GetMapping("/api/riders/{id}")
	  public ResponseEntity<Riders> getRiderById(@PathVariable("id") long id) {
	    Optional<Riders> riderData = ridersDao.findById(id);

	    if (riderData.isPresent()) {
	      return new ResponseEntity<>(riderData.get(), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @PostMapping("/api/riders")
	  public ResponseEntity<Riders> addRider(@RequestBody Riders rider) {
	      try {
	          Optional<Riders> existingRider = ridersDao.findByEmail(rider.getEmail());
	          if (existingRider.isPresent()) {
	              // Return an error response indicating that the email already exists
	              return ResponseEntity.status(HttpStatus.CONFLICT).build();
	          }

	          Riders newRider = new Riders(rider.getId(), rider.getEmail(), rider.getPassword());
	          Riders savedRider = ridersDao.save(newRider);
	          return ResponseEntity.status(HttpStatus.CREATED).body(savedRider);
	      } catch (Exception e) {
	          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	      }
	  }

	  
	  
	  @PostMapping("/api/riders/login")
	    public ResponseEntity<Riders> login(@RequestBody Riders rider) {
	        try {
	            Optional<Riders> existingRider = ridersDao.findByEmail(rider.getEmail());
	            if (existingRider.isPresent() && existingRider.get().getPassword().equals(rider.getPassword())) {
	                // Return a success response indicating that the credentials are correct
	                return ResponseEntity.status(HttpStatus.OK).body(existingRider.get());
	            } else {
	                // Return an error response indicating that the credentials are incorrect
	                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	            }
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	        }
	    }
	  

	  @PutMapping("/api/riders/{id}")
	  public ResponseEntity<Riders> updateRider(@PathVariable("id") long id, @RequestBody Riders rider) {
	    Optional<Riders> riderData = ridersDao.findById(id);

	    if (riderData.isPresent()) {
	      Riders rider1 = riderData.get();
	      rider1.setEmail(rider.getEmail());
	      rider1.setPassword(rider.getPassword());
	      return new ResponseEntity<>(ridersDao.save(rider1), HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

	  @DeleteMapping("/api/riders/{id}")
	  public ResponseEntity<HttpStatus> deleteRider(@PathVariable("id") long id) {
	    try {
	      ridersDao.deleteById(id);
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }

	  @DeleteMapping("/api/riders")
	  public ResponseEntity<HttpStatus> deleteAllRiders() {
	    try {
	      ridersDao.deleteAll();
	      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	    } catch (EmptyResultDataAccessException e) {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } catch (Exception e) {
	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	  }


//	  @GetMapping("/tutorials/published")
//	  public ResponseEntity<List<Tutorial>> findByPublished() {
//	    try {
//	      List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
//
//	      if (tutorials.isEmpty()) {
//	        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//	      }
//	      return new ResponseEntity<>(tutorials, HttpStatus.OK);
//	    } catch (Exception e) {
//	      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//	    }
//	  }

	}