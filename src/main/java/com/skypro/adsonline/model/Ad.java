package com.skypro.adsonline.model;

import com.skypro.adsonline.dto.AdDTO;
import com.skypro.adsonline.dto.CreateAdDTO;
import com.skypro.adsonline.dto.FullAdDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.beans.BeanUtils;
import javax.persistence.*;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Entity
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
    private String description;
    private Integer price;
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    private User author;

    @OneToMany(mappedBy = "ad", cascade = {CascadeType.ALL})
    private List<Comment> comments;

    @Lob
    @Type(type = "org.hibernate.type.BinaryType")
    private byte[] image;



    public static Ad fromCreateAdDTO(CreateAdDTO createAdDTO) {
        Ad ad = new Ad();
        BeanUtils.copyProperties(createAdDTO, ad);
        return ad;
    }

    public FullAdDTO toFullDTO() {
        FullAdDTO fullAds = new FullAdDTO();
        BeanUtils.copyProperties(this, fullAds);
        fullAds.setEmail(this.getAuthor().getUsername());
        fullAds.setPhone(this.getAuthor().getPhone());
        fullAds.setImage("/ads/image/" + this.getPk());
        return fullAds;
    }


    public AdDTO toDTO() {
        AdDTO adDTO = new AdDTO();
        BeanUtils.copyProperties(this, adDTO);
        adDTO.setAuthor(this.getAuthor().getId());
        adDTO.setImage("/ads/image/" + this.getPk());
        return adDTO;
    }
}
