package net.csdcodes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private MovieRepository repository;

    @GetMapping("/list")
    private String getAll(Model model){
        model.addAttribute("movies", repository.findAll());
        return "movie_list";
    }

    @GetMapping(path = {"/add", "edit/{id}"})
    private String addForm(@PathVariable("id") Optional<Long> id, Model model){
        if(id.isPresent()){
            model.addAttribute("movie", repository.findById(id.get()));
        }else{
            model.addAttribute("movie", new Movie());
        }
        return "add_edit_movie";
    }

    @PostMapping("/addEdit")
    private String insertOrUpdate(Movie movie){
        if(movie.getId() == null){
            repository.save(movie);
        }else{
            Optional<Movie> movieOptional = repository.findById(movie.getId());
            if(movieOptional.isPresent()){
                Movie editMovie = movieOptional.get();
                editMovie.setCountry(movie.getCountry());
                editMovie.setLanguage(movie.getLanguage());
                editMovie.setReleaseYear(movie.getReleaseYear());
                editMovie.setTitle(movie.getTitle());
                editMovie.setTags(movie.getTags());
                repository.save(editMovie);
            }
        }
        return "redirect:/movie/list";
    }

    @GetMapping("/delete/{id}")
    private String deleteMovie(@PathVariable("id") Long id){
        Optional<Movie> movie = repository.findById(id);
        if(movie.isPresent()){
            repository.delete(movie.get());
        }else{
            System.err.println("not found");
        }
        return "redirect:/movie/list";
    }

}
