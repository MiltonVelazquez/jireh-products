@RestController
@RequestMapping(path = "products/wishlist")
public class WishListController {

    @Autowired
    private WishListRepository wishListRepository;

    @Autowired
    private ProductService productService;

    private Long getUserIdFromJwt(Jwt principal) {
        Object userIdClaim = principal.getClaim("id");
        if (userIdClaim == null) {
            userIdClaim = principal.getClaim("userId");
        }

        if (userIdClaim instanceof Number) {
            return ((Number) userIdClaim).longValue();
        } else if (userIdClaim instanceof String) {
            return Long.valueOf((String) userIdClaim);
        }
        
        throw new IllegalStateException("El token JWT no incluye un claim 'id' numérico.");
    }

    @PostMapping
    public ResponseEntity<ProductDTO> save(@RequestBody WishListEntity wishListEntity, @AuthenticationPrincipal Jwt principal){
        Long userId = getUserIdFromJwt(principal);
        wishListEntity.setUserId(userId);

        WishListEntity saved = wishListRepository.save(wishListEntity);
        return ResponseEntity.status(201).body(productService.convertToDTO(saved.getProduct()));
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getUserWishList(@AuthenticationPrincipal Jwt principal) {
        Long userId = getUserIdFromJwt(principal);
        List<WishListEntity> wishList = wishListRepository.findByUserId(userId);
        
        List<ProductDTO> products = wishList.stream()
                .map(item -> productService.convertToDTO(item.getProduct())) 
                .toList();

        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<Void> deleteByProductId(@PathVariable Long productId, @AuthenticationPrincipal Jwt principal) {
        Long userId = getUserIdFromJwt(principal);

        WishListEntity wishListItem = wishListRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("El producto no se encuentra en tu lista de deseos"));

        wishListRepository.delete(wishListItem);

        return ResponseEntity.noContent().build();
    }
}
