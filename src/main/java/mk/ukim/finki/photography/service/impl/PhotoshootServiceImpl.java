package mk.ukim.finki.photography.service.impl;

import mk.ukim.finki.photography.model.Photoshoot;
import mk.ukim.finki.photography.model.User;
import mk.ukim.finki.photography.repository.PhotoshootRepository;
import mk.ukim.finki.photography.service.PhotoshootService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class PhotoshootServiceImpl implements PhotoshootService {

    private final PhotoshootRepository repository;

    public PhotoshootServiceImpl(PhotoshootRepository repository) {
        this.repository = repository;
    }

    @Override
    public Photoshoot findById(Long id) {
        return this.repository.findById(id).orElseThrow();
    }

    @Override
    public Photoshoot save(Photoshoot photoshoot) {
        return this.repository.save(photoshoot);
    }

    @Override
    @Transactional
    public List<Photoshoot> findByPhotographer(User photographer) {
        return this.repository.findAllByPhotographer(photographer);
    }
}
