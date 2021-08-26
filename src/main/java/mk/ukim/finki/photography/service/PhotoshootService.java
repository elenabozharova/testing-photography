package mk.ukim.finki.photography.service;

import mk.ukim.finki.photography.model.Photoshoot;
import mk.ukim.finki.photography.model.User;

import javax.transaction.Transactional;
import java.util.List;

public interface PhotoshootService {
    public Photoshoot findById(Long id);

    public Photoshoot save(Photoshoot photoshoot);

    @Transactional
    public List<Photoshoot> findByPhotographer(User photographer);
}
